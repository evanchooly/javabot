package javabot

import com.antwerkz.sofia.Sofia
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Singleton
import com.jayway.awaitility.Awaitility
import javabot.commands.AdminCommand
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.EventDao
import javabot.dao.LogsDao
import javabot.model.State
import javabot.dao.ShunDao
import javabot.database.UpgradeScript
import javabot.model.Channel
import javabot.model.JavabotUser
import javabot.model.Logs
import javabot.model.Logs.Type
import javabot.operations.BotOperation
import javabot.operations.OperationComparator
import javabot.operations.StandardOperation
import javabot.operations.throttle.NickServViolationException
import javabot.operations.throttle.Throttler
import javabot.web.JavabotApplication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.time.LocalDateTime
import java.util.ArrayList
import java.util.SortedMap
import java.util.TreeMap
import java.util.TreeSet
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

@Singleton
open class Javabot @Inject
    constructor(var injector: Injector, var configDao: ConfigDao, var channelDao: ChannelDao,
            var logsDao: LogsDao, var shunDao: ShunDao, var eventDao: EventDao,
            var throttler: Throttler, var adapter: IrcAdapter, var adminDao: AdminDao,
            var javabotConfig: JavabotConfig, var application: Provider<JavabotApplication>) {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(Javabot::class.java)

        @JvmStatic fun main(args: Array<String>) {
            Sofia.javabotStart()
            val injector = Guice.createInjector(JavabotModule())
            val bot = injector.getInstance(Javabot::class.java)
            bot.start()
            Awaitility.await()
                    .forever()
                    .until<Boolean> { !bot.isRunning() }
        }
    }

    private var allOperationsMap = TreeMap<String, BotOperation>()

    val startString: String by lazy {
        configDao.get().trigger
    }

    open val nick: String by lazy {
        configDao.get().nick
    }

    val executors = ThreadPoolExecutor(5, 10, 5L, TimeUnit.MINUTES, ArrayBlockingQueue(50),
            JavabotThreadFactory(true, "javabot-handler-thread-"))

    private val eventHandler = Executors.newScheduledThreadPool(2, JavabotThreadFactory(true, "javabot-event-handler"))

    private val ignores = ArrayList<String>()

    val activeOperations = sortedSetOf(OperationComparator())

    @Volatile private var running = true

    init {
        val hook = Thread(Runnable { this.shutdown() })
        hook.isDaemon = false
        Runtime.getRuntime().addShutdownHook(hook)
    }

    open fun start() {
        enableOperations()
        setUpThreads()
        applyUpgradeScripts()
        connect()
        startWebApp()
    }

    fun setUpThreads() {
        eventHandler.scheduleAtFixedRate({ this.processAdminEvents() }, 1, 5, TimeUnit.SECONDS)
        eventHandler.scheduleAtFixedRate({ this.joinChannels() }, 1, 5, TimeUnit.SECONDS)
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

    open fun joinChannels() {
        if (adapter.isConnected()) {
            val joined = mutableSetOf<String>()
            val channels = ArrayList(channelDao.getChannels(true))
            if (joined.size != channels.size) {
                channels.filter({ channel -> !joined.contains(channel.name) }).forEach({ channel ->
                    if (!adapter.isBotOnChannel(channel.name)) {
                        joinChannel(channel)
                        Thread.sleep(250L)
                    } else {
                        joined.add(channel.name)
                    }
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

    fun isRunning(): Boolean {
        return running
    }

    fun connect() {
        try {
            val thread = Thread {
                try {
                    adapter.startBot()
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

    fun getAllOperations(): SortedMap<String, BotOperation> {
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
        val responses = arrayListOf<Message>()
        logMessage(message.channel, sender, message.value)

        if (!ignores.contains(sender.nick) && !shunDao.isShunned(sender.nick)
                && (message.channel != null || isOnCommonChannel(message.user))) {
            try {
                if (message.triggered) {
                    if (throttler.isThrottled(message.user) && !adminDao.isAdmin(message.user)) {
                        privateMessageUser(sender, Sofia.throttledUser())
                    } else {
                        responses.addAll(getResponses(message))
                    }
                }
            } catch (e: NickServViolationException) {
                privateMessageUser(message.user, e.message!!)
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
                logMessage(event.channel, event.user, value)
                adapter.send(event.channel, value)
            } else {
                LOG.debug("channel is null.  sending directly to user: " + event)
                privateMessageUser(event.user, value)
            }
        }
    }

    fun privateMessageUser(user: JavabotUser, message: String) {
        logMessage(null, user, message)
        adapter.send(user, message)
    }

    private fun postAction(channel: Channel, message: String) {
        if (channel.name != nick) {
            logsDao.logMessage(Type.ACTION, channel, JavabotUser(nick), message)
        }
        adapter.action(channel, message)
    }

    internal fun logMessage(channel: Channel?, user: JavabotUser?, message: String) {
        if (channel?.name != nick) {
            logsDao.logMessage(Logs.Type.MESSAGE, channel, user, message)
        }
    }

    fun getResponses(message: Message): List<Message> {
        val iterator = activeOperations.iterator()
        val responses = arrayListOf<Message>()
        while (iterator.hasNext() && responses.isEmpty()) {
            val next = iterator.next()
            try {
                responses.addAll(next.handleMessage(message))
            } catch (e: Exception) {
                LOG.error("NPE: message = [$message], requester = [${message.user}]", e)
            }

        }

        if (responses.isEmpty() && (!message.value.toLowerCase().startsWith("${nick}'s".toLowerCase()))) {
            responses.add(Message(message.channel, message.user, Sofia.unhandledMessage(message.user.nick)))
        }
        return responses
    }

    private fun getChannelResponses(event: Message): List<Message> {
        val iterator = activeOperations.iterator()
        val responses = arrayListOf<Message>()
        while (iterator.hasNext() && responses.isEmpty()) {
            responses.addAll(iterator.next().handleChannelMessage(event))
        }
        return responses
    }

    open fun isOnCommonChannel(user: JavabotUser): Boolean {
        return adapter.isOnCommonChannel(user)
    }

    fun joinChannel(channel: Channel) {
        adapter.joinChannel(channel)
    }

    fun leaveChannel(chan: Channel, user: JavabotUser) {
        adapter.leave(chan, user)
    }

    fun message(target: String, message: String) {
        adapter.message(target, message)
    }

    open fun getUser(nick: String): JavabotUser {
        return adapter.getUser(nick)
    }

}
