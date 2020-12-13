package javabot

import com.antwerkz.sofia.Sofia
import com.google.inject.Injector
import com.jayway.awaitility.Awaitility
import com.jayway.awaitility.Duration
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChangeDao
import javabot.dao.ChannelDao
import javabot.dao.EventDao
import javabot.dao.LogsDao
import javabot.dao.NickServDao
import javabot.model.Admin
import javabot.model.AdminEvent
import javabot.model.ApiEvent
import javabot.model.Change
import javabot.model.Channel
import javabot.model.JavabotUser
import javabot.model.Logs
import javabot.model.NickServInfo
import javabot.model.State
import javabot.model.javadoc.JavadocApi
import dev.morphia.Datastore
import org.pircbotx.PircBotX
import org.slf4j.LoggerFactory
import org.testng.Assert
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeMethod
import org.testng.annotations.BeforeTest
import org.testng.annotations.Guice
import java.util.EnumSet
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

@Guice(modules = [JavabotTestModule::class])
open class BaseTest {

    companion object {
        const val TEST_TARGET_NICK: String = "jbtestuser"
        const val TEST_USER_NICK: String = "botuser"
        const val TEST_NON_ADMIN_USER_NICK = "nonadminuser"
        const val TEST_BOT_NICK: String = "testjavabot"
        const val BOT_EMAIL: String = "test@example.com"
        val DONE: EnumSet<State> = EnumSet.of(State.COMPLETED, State.FAILED)
        val OK: String = Sofia.ok(TEST_USER_NICK.take(16))
        val TARGET_USER = JavabotUser(TEST_TARGET_NICK, TEST_TARGET_NICK, "hostmask")
        val TEST_USER = JavabotUser(TEST_USER_NICK, TEST_USER_NICK, "hostmask")
        val TEST_NON_ADMIN_USER = JavabotUser(TEST_NON_ADMIN_USER_NICK, TEST_NON_ADMIN_USER_NICK, "hostmask")
        val TEST_CHANNEL = Channel("#jbunittest")
        private val LOG = LoggerFactory.getLogger(BaseTest::class.java)
    }

    val testIrcChannel: MockIrcChannel by lazy {
        MockIrcChannel(ircBot.get(), TEST_CHANNEL.name)
    }

    val testIrcUser: MockIrcUser by lazy {
        MockIrcUser(ircBot.get(), TEST_USER.nick)
    }

    val testIrcHostmask: MockUserHostmask by lazy {
        MockUserHostmask(ircBot.get(), TEST_USER.nick)
    }

    @Inject
    lateinit var injector: Injector

    @Inject
    protected lateinit var datastore: Datastore

    @Inject
    private lateinit var config: JavabotConfig

    @Inject
    protected lateinit var apiDao: ApiDao

    @Inject
    protected lateinit var eventDao: EventDao

    @Inject
    protected lateinit var channelDao: ChannelDao

    @Inject
    protected lateinit var logsDao: LogsDao

    @Inject
    protected lateinit var adminDao: AdminDao

    @Inject
    protected lateinit var changeDao: ChangeDao

    @Inject
    protected lateinit var bot: Provider<TestJavabot>

    @Inject
    protected lateinit var ircBot: Provider<PircBotX>

    @Inject
    protected lateinit var messages: Messages


    @BeforeTest
    fun setup() {
        LOG.debug("setting up test")
        messages.clear()
        val admin = adminDao.getAdminByEmailAddress(BOT_EMAIL) ?: Admin(TEST_USER.nick, BOT_EMAIL, TEST_USER.hostmask, true)
        admin.ircName = TEST_USER.nick
        admin.emailAddress = BOT_EMAIL
        admin.hostName = TEST_USER.hostmask
        admin.botOwner = true

        adminDao.save(admin)

        var channel: Channel? = channelDao.get(TEST_CHANNEL.name)
        if (channel == null) {
            channel = Channel()
            channel.name = TEST_CHANNEL.name
            channel.logged = true
            channelDao.save(channel)
        }

        logsDao.getQuery(Logs::class.java).delete()
        changeDao.getQuery(Change::class.java).delete()
        bot.get().start()
    }

    protected fun enableAllOperations() {
        val bot = this.bot.get()
        bot.getAllOperations().keys.forEach { bot.enableOperation(it) }
    }

    protected fun disableAllOperations() {
        val bot = this.bot.get()
        bot.getAllOperations().keys.forEach { bot.disableOperation(it) }
    }

    @BeforeMethod
    fun clearMessages() {
        messages.clear()
    }

    @AfterSuite
    fun shutdown() {
        bot.get().shutdown()
    }

    protected fun waitForEvent(event: AdminEvent, alias: String, timeout: Duration) {
        Awaitility.await(alias)
                .atMost(timeout)
                .pollInterval(1, TimeUnit.SECONDS)
                .until<Boolean> {
                    val found = eventDao.find(event.id)
                    DONE.contains(found?.state)
                }
    }

    protected fun message(value: String, start: String = "~", user: JavabotUser = TEST_USER): Message {
        return Message.extractContentFromMessage(bot.get(), TEST_CHANNEL, user, start, bot.get().nick, value)
    }

    protected fun privateMessage(value: String, user: JavabotUser = TEST_USER): Message {
        return Message(user, value)
    }

    protected fun scanForResponse(messages: List<Message>, target: String) {
        var found = false
        for (response in messages) {
            found = found or response.value.contains(target)
        }
        Assert.assertTrue(found, java.lang.String.format("Did not find \n'%s' in \n'%s'", target,
                messages.joinToString("\n") { it.value }))
    }

    protected fun loadApi(apiName: String, groupId: String = "", artifactId: String = "", version: String): JavadocApi {
        return apiDao.find(apiName) ?: {
            LOG.info("$apiName not found.  Generating now.")
            val api = JavadocApi(config, apiName, groupId, artifactId, version)
            apiDao.save(api)
            val event = ApiEvent.add(TEST_USER.nick, api)
            injector.injectMembers(event)
            event.handle()
            messages.clear()
            LOG.info("$apiName finished.")
            api
        }.invoke()
    }
}

fun NickServDao.registerIrcUser(nick: String, userName: String, host: String): JavabotUser {
    val bob = JavabotUser(nick, userName, host)
    val info = NickServInfo(bob)
    info.registered = info.registered.minusDays(100)
    clear()
    save(info)
    return bob
}
