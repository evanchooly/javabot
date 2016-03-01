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
import javabot.model.AdminEvent.State
import javabot.model.Change
import javabot.model.Channel
import javabot.model.Logs
import javabot.model.NickServInfo
import javabot.model.UserFactory
import org.mongodb.morphia.Datastore
import org.pircbotx.PircBotX
import org.pircbotx.User
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

    var done: EnumSet<State> = EnumSet.of(State.COMPLETED, State.FAILED)

    @Inject
    protected lateinit var userFactory: UserFactory

    @Inject
    protected lateinit var datastore: Datastore

    @Inject
    protected lateinit var eventDao: EventDao

    @Inject
    protected lateinit var channelDao: ChannelDao

    @Inject
    protected lateinit var logsDao: LogsDao

    @Inject
    protected lateinit var nickServDao: NickServDao

    @Inject
    protected lateinit var ircBot: Provider<PircBotX>

    @Inject
    protected lateinit var adminDao: AdminDao

    @Inject
    protected lateinit var changeDao: ChangeDao

    @Inject
    protected lateinit var bot: Provider<TestJavabot>

    @Inject
    protected lateinit var messages: Messages

    val ok: String = "OK, " + TEST_USER_NICK.substring(0, Math.min(TEST_USER_NICK.length, 16)) + "."

    val targetUser: User by lazy { userFactory.createUser(TEST_TARGET_NICK, TEST_TARGET_NICK, "hostmask") }

    val testUser: User by lazy { userFactory.createUser(TEST_USER_NICK, TEST_USER_NICK, "hostmask") }

    val testChannel: org.pircbotx.Channel by lazy { getIrcBot().userChannelDao.getChannel("#jbunittest") }

    @BeforeTest fun setup() {
        messages.clear()
        var admin = try {
            adminDao.getAdminByEmailAddress(BOT_EMAIL)
        } catch(adminNotFound: RuntimeException) {
            Admin(testUser.nick, BOT_EMAIL, testUser.hostmask, true)
        }
        admin.ircName = testUser.nick
        admin.emailAddress = BOT_EMAIL
        admin.hostName = testUser.hostmask
        admin.botOwner = true

        adminDao.save(admin)

        var channel: Channel? = channelDao.get(testChannel.name)
        if (channel == null) {
            channel = Channel()
            channel.name = testChannel.name
            channel.logged = true
            channelDao.save(channel)
        }

        datastore.delete(logsDao.getQuery(Logs::class.java))
        datastore.delete(changeDao.getQuery(Change::class.java))
        bot.get().start()
        enableAllOperations()
    }

    protected fun enableAllOperations() {
        val bot = this.bot.get()
        bot.getAllOperations().keys.forEach({ bot.enableOperation(it) })
    }

    protected fun disableAllOperations() {
        val bot = this.bot.get()
        bot.getAllOperations().keys.forEach({ bot.disableOperation(it) })
    }

    @BeforeMethod fun clearMessages() {
        messages.clear()
    }

    fun getIrcBot(): PircBotX {
        return ircBot.get()
    }

    @AfterSuite
    @Throws(InterruptedException::class) fun shutdown() {
        bot.get().shutdown()
    }

    protected fun waitForEvent(event: AdminEvent, alias: String, timeout: Duration) {
        Awaitility.await(alias)
              .atMost(timeout)
              .pollInterval(5, TimeUnit.SECONDS)
              .until<Boolean> {
                  done.contains(eventDao.find(event.id)?.state)
              }
    }

    protected fun registerIrcUser(nick: String, userName: String, host: String): User {
        val bob = userFactory.createUser(nick, userName, host)
        val info = NickServInfo(bob)
        info.registered = info.registered.minusDays(100)
        nickServDao.clear()
        nickServDao.save(info)
        return bob
    }


    protected fun message(value: String, user: User = testUser, startString: String = ""): Message {
        return Message(testChannel, user, value).extractContentFromMessage(getIrcBot(), startString)!!
    }

    protected fun scanForResponse(messages: List<Message>, target: String) {
        var found = false
        for (response in messages) {
            found = found or response.value.contains(target)
        }
        Assert.assertTrue(found, java.lang.String.format("Did not find \n'%s' in \n'%s'", target,
                messages.map({ it.value }).joinToString { "\n" }))
    }

    companion object {
        val TEST_TARGET_NICK: String = "jbtestuser"
        val TEST_USER_NICK: String = "botuser"
        val TEST_BOT_NICK: String = "testjavabot"
        val BOT_EMAIL: String = "test@example.com"
    }
}