package javabot

import com.jayway.awaitility.Awaitility

import com.jayway.awaitility.Duration
import javabot.dao.AdminDao
import javabot.dao.ChangeDao
import javabot.dao.ChannelDao
import javabot.dao.EventDao
import javabot.dao.LogsDao
import javabot.dao.NickServDao
import javabot.model.Admin
import javabot.model.AdminEvent
import javabot.model.State
import javabot.model.Change
import javabot.model.Channel
import javabot.model.JavabotUser
import javabot.model.Logs
import javabot.model.NickServInfo
import org.mongodb.morphia.Datastore
import org.pircbotx.PircBotX
import org.testng.Assert
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeMethod
import org.testng.annotations.BeforeTest
import org.testng.annotations.Guice
import java.util.EnumSet
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

@Guice(modules = arrayOf(JavabotTestModule::class))
open class BaseTest {

    companion object {
        val TEST_TARGET_NICK: String = "jbtestuser"
        val TEST_USER_NICK: String = "botuser"
        val TEST_NON_ADMIN_USER_NICK = "nonadminuser"
        val TEST_BOT_NICK: String = "testjavabot"
        val BOT_EMAIL: String = "test@example.com"
        val DONE: EnumSet<State> = EnumSet.of(State.COMPLETED, State.FAILED)
        val OK: String = "OK, " + TEST_USER_NICK.substring(0, Math.min(TEST_USER_NICK.length, 16)) + "."
        val TARGET_USER = JavabotUser(TEST_TARGET_NICK, TEST_TARGET_NICK, "hostmask")
        val TEST_USER = JavabotUser(TEST_USER_NICK, TEST_USER_NICK, "hostmask")
        val TEST_NON_ADMIN_USER = JavabotUser(TEST_NON_ADMIN_USER_NICK, TEST_NON_ADMIN_USER_NICK, "hostmask")
        val TEST_CHANNEL = Channel("#jbunittest")
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
    protected lateinit var datastore: Datastore

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

        datastore.delete(logsDao.getQuery(Logs::class.java))
        datastore.delete(changeDao.getQuery(Change::class.java))
        bot.get().start()
    }

    protected fun enableAllOperations() {
        val bot = this.bot.get()
        bot.getAllOperations().keys.forEach({ bot.enableOperation(it) })
    }

    protected fun disableAllOperations() {
        val bot = this.bot.get()
        bot.getAllOperations().keys.forEach({ bot.disableOperation(it) })
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
                .pollInterval(10, TimeUnit.SECONDS)
                .until<Boolean> {
                    val found = eventDao.find(event.id)
                    DONE.contains(found?.state)
                }
    }

    protected fun message(value: String, start: String = "~", user: JavabotUser = TEST_USER): Message {
        return Message.extractContentFromMessage(bot.get(), TEST_CHANNEL, user, start, bot.get().nick, value)
    }

    protected fun privateMessage(value: String, start: String = "~", user: JavabotUser = TEST_USER): Message {
        return Message(user, value)
    }

    protected fun scanForResponse(messages: List<Message>, target: String) {
        var found = false
        for (response in messages) {
            found = found or response.value.contains(target)
        }
        Assert.assertTrue(found, java.lang.String.format("Did not find \n'%s' in \n'%s'", target,
                messages.map({ it.value }).joinToString("\n")))
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
