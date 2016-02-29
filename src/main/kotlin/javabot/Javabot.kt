package javabot

import com.antwerkz.sofia.Sofia
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Singleton
import com.jayway.awaitility.Awaitility
import javabot.commands.AdminCommand
import javabot.dao.*
import javabot.database.UpgradeScript
import javabot.web.JavabotApplication
import javabot.model.AdminEvent.State
import javabot.model.Logs
import javabot.model.Logs.Type
import javabot.operations.BotOperation
import javabot.operations.OperationComparator
import javabot.operations.StandardOperation
import javabot.operations.throttle.NickServViolationException
import javabot.operations.throttle.Throttler
import org.pircbotx.PircBotX
import org.pircbotx.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

@Singleton
open class Javabot @Inject
    constructor(protected var injector: Injector, private var configDao: ConfigDao, private var channelDao: ChannelDao,
                private var logsDao: LogsDao, private var shunDao: ShunDao, private var eventDao: EventDao,
                private var throttler: Throttler, private var ircBot: Provider<PircBotX>,
                private var javabotConfig: JavabotConfig, private var application: Provider<JavabotApplication>) {

    private var allOperationsMap = TreeMap<String, BotOperation>()

    val startStrings: List<String> by lazy {
        listOf(getNick(), "~")
    }

    val executors = ThreadPoolExecutor(5, 10, 5L, TimeUnit.MINUTES, ArrayBlockingQueue(50),
            JavabotThreadFactory(true, "javabot-handler-thread-"))

    private val eventHandler = Executors.newScheduledThreadPool(2, JavabotThreadFactory(true, "javabot-event-handler"))

    private val ignores = ArrayList<String>()

    val activeOperations = TreeSet(OperationComparator())

    @Volatile private var running = true

    init {
        val hook = Thread(Runnable { this.shutdown() })
        hook.isDaemon = false
        Runtime.getRuntime().addShutdownHook(hook)
    }

    fun start() {
        enableOperations()
        setUpThreads()
        applyUpgradeScripts()
        connect()
        startWebApp()
    }

    private fun setUpThreads() {
        eventHandler.scheduleAtFixedRate({ this.processAdminEvents() }, 5, 5, TimeUnit.SECONDS)
        eventHandler.scheduleAtFixedRate({ this.joinChannels() }, 5, 60, TimeUnit.SECONDS)
    }

    protected fun processAdminEvents() {
        val event = eventDao.findUnprocessed()
        if (event != null) {
            try {
                event.state = State.PROCESSING
                eventDao.save(event)
                injector.injectMembers(event)
                event.handle()
                event.state = State.COMPLETED
            } catch (e: Exception) {
                event.state = State.FAILED
                LOG.error(e.message, e)
            }

            event.completed = LocalDateTime.now()
            eventDao.save(event)
        }
    }

    private fun joinChannels() {
        val ircBot = this.ircBot.get()
        val connected = ircBot.isConnected
        if (connected) {
            val joined = ircBot.userChannelDao.allChannels.map({ it.name }).toSet()
            val channels = channelDao.getChannels(true)
            if (joined.size != channels.size) {
                channels.filter({ channel -> !joined.contains(channel.name) }).forEach({ channel ->
                    channel.join(ircBot)
                    Thread.sleep(500L)
                })
            }
        }
    }

    fun shutdown() {
        if (!executors.isShutdown) {
            executors.shutdown()
            try {
                executors.awaitTermination(10, TimeUnit.SECONDS)
            } catch (e: InterruptedException) {
                LOG.error(e.message, e)
            }

            running = false
        }
    }

    private fun isRunning(): Boolean {
        return running
    }

    fun connect() {
        try {
            val thread = Thread {
                try {
                    ircBot.get().startBot()
                } catch (e: Exception) {
                    e.printStackTrace(System.out)
                }
            }
            thread.start()
        } catch (ex: Exception) {
            ex.printStackTrace(System.out)
        }

    }

    fun startWebApp() {
        if (javabotConfig.startWebApp()) {
            if (File("javabot.yml").exists()) {
                try {
                    Sofia.logWebappStarting()
                    application.get().run(arrayOf("server", "javabot.yml"))
                } catch (e: Exception) {
                    throw RuntimeException(e.message, e)
                }
            } else {
                println(Sofia.configurationWebMissingFile())
            }
        } else {
            Sofia.logWebappNotStarting()
        }
    }

    protected fun applyUpgradeScripts() {
        val set = TreeSet(ScriptComparator())
        set.addAll(configDao.list(UpgradeScript::class.java))
        set.forEach({ it.execute() })
    }

    @SuppressWarnings("unchecked") fun getAllOperations(): SortedMap<String, BotOperation> {
        for (op in configDao.list(BotOperation::class.java)) {
            allOperationsMap.put(op.getName(), op)
        }
        return allOperationsMap
    }

    fun enableOperations() {
        try {
            val allOperations = getAllOperations()
            configDao.get().operations.forEach { klass ->
                val get = allOperations[klass]
                if (get != null) {
                    activeOperations.add(get)
                }
            }
        } catch (e: Exception) {
            LOG.error(e.message, e)
            throw RuntimeException(e.message, e)
        }

    }

    fun disableOperation(name: String?): Boolean {
        var disabled = false
        val operation = getAllOperations()[name]
        if (operation != null && operation !is AdminCommand && operation !is StandardOperation) {
            activeOperations.remove(operation)
            val config = configDao.get()
            config.operations.remove(name)
            configDao.save(config)
            disabled = true
        }
        return disabled
    }

    fun enableOperation(name: String) {
        val op = getAllOperations()[name]
        if (op != null) {
            val config = configDao.get()
            config.operations.add(name)
            activeOperations.add(op)
            configDao.save(config)
        } else {
            println("Operation not found: ${name}.  \nKnown ops: ${getAllOperations().keys}")
            System.exit(-1)
        }
    }

    fun processMessage(message: Message) {
        val sender = message.user
        val channel = message.channel
        logsDao.logMessage(Logs.Type.MESSAGE, channel, sender, message.value)
        val responses = arrayListOf<Message>()
        if (isValidSender(sender.nick)) {
            for (startString in startStrings) {
                if (message.value.startsWith(startString)) {
                    try {
                        if (throttler.isThrottled(message.user)) {
                            privateMessageUser(message.user, Sofia.throttledUser())
                        } else {

                            val content = message.extractContentFromMessage(getIrcBot(), startString)
                            if (content != null) {
                                responses.addAll(getResponses(content))
                            }
                        }
                    } catch (e: NickServViolationException) {
                        privateMessageUser(message.user, e.message!!)
                    }

                }
            }
            if (responses.isEmpty()) {
                responses.addAll(getChannelResponses(message))
            }
            responses.forEach {
                postMessage(it)
            }
        } else {
            if (LOG.isInfoEnabled) {
                LOG.info("ignoring " + sender)
            }
        }
    }

    fun addIgnore(sender: String) {
        ignores.add(sender)
    }

    private fun postMessage(event: Message) {
        if (event is Action) {
            postAction(event.channel!!, event.value)
        } else {
            val value = event.massageTell()
            if (event.channel != null) {
                logMessage(event.channel, getIrcBot().userBot, value)
                event.channel.send().message(value)
            } else {
                LOG.debug("channel is null.  sending directly to user: " + event)
                privateMessageUser(event.user, value)
            }
        }
    }

    fun privateMessageUser(user: User, message: String) {
        logMessage(null, user, message)
        user.send().message(message)
    }

    private fun postAction(channel: org.pircbotx.Channel, message: String) {
        val bot = ircBot.get().userBot
        if (channel.name != bot.nick) {
            logsDao.logMessage(Type.ACTION, channel, bot, message)
        }
        channel.send().action(message)
    }

    internal fun logMessage(channel: org.pircbotx.Channel?, user: User, message: String) {
        val sender = ircBot.get().nick
        if (channel != null && channel.name != sender) {
            logsDao.logMessage(Logs.Type.MESSAGE, channel, user, message)
        }
    }

    fun getResponses(message: Message): List<Message> {
        val iterator = activeOperations.iterator()
        var responses = arrayListOf<Message>()
        while (iterator.hasNext() && responses.isEmpty()) {
            val next = iterator.next()
            try {
                responses.addAll(next.handleMessage(message))
            } catch (e: Exception) {
                LOG.error("NPE: message = [$message], requester = [${message.user}]")
                e.printStackTrace()
            }

        }

        if (responses.isEmpty()) {
            responses.add(Message(message.channel, message.user, Sofia.unhandledMessage(message.user.nick)))
        }
        return responses
    }

    private fun getChannelResponses(event: Message): List<Message> {
        val iterator = activeOperations.iterator()
        var responses = arrayListOf<Message>()
        while (iterator.hasNext() && responses.isEmpty()) {
            responses.addAll(iterator.next().handleChannelMessage(event))
        }
        return responses
    }

    open fun isOnCommonChannel(user: User): Boolean {
        return !ircBot.get().userChannelDao.getChannels(user).isEmpty()
    }

    protected fun isValidSender(sender: String): Boolean {
        return !ignores.contains(sender) && !shunDao.isShunned(sender)
    }

    open fun getNick(): String {
        return configDao.get().nick
    }

    open fun getIrcBot(): PircBotX {
        return ircBot.get()
    }

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(Javabot::class.java)

        @JvmStatic fun main(args: Array<String>) {
            try {
                val injector = Guice.createInjector(JavabotModule())
                if (LOG.isInfoEnabled) {
                    LOG.info("Starting Javabot")
                }
                val bot = injector.getInstance(Javabot::class.java)
                bot.start()
                Awaitility.await()
                        .forever()
                        .until<Boolean> { !bot.isRunning() }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
