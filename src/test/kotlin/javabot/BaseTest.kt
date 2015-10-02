package javabot

import com.jayway.awaitility.Awaitility
import com.jayway.awaitility.Duration
import javabot.dao.AdminDao
import javabot.dao.ChangeDao
import javabot.dao.ChannelDao
import javabot.dao.EventDao
import javabot.dao.LogsDao
import javabot.dao.NickServDao
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
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeMethod
import org.testng.annotations.BeforeTest
import org.testng.annotations.Guice
import java.util.EnumSet
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

@Guice(modules = arrayOf(JavabotTestModule::class))
public open class BaseTest {

    public var done: EnumSet<State> = EnumSet.of(State.COMPLETED, State.FAILED)

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
    protected lateinit var javabot: Provider<TestJavabot>

    @Inject
    protected lateinit var messages: Messages

    public val ok: String = "OK, " + TEST_USER_NICK.substring(0, Math.min(TEST_USER_NICK.length(), 16)) + "."

    val testUser: User by lazy { userFactory.createUser(TEST_USER_NICK, TEST_USER_NICK, "hostmask") }

    val javabotChannel: org.pircbotx.Channel by lazy { getIrcBot().userChannelDao.getChannel("#jbunittest") }

    @BeforeTest
    public fun setup() {
        val admin = adminDao.getAdminByEmailAddress(BOT_EMAIL)!!
        admin.ircName = testUser.nick
        admin.emailAddress = BOT_EMAIL
        admin.hostName = testUser.hostmask
        admin.botOwner = true
        adminDao.save(admin)

        var channel: Channel? = channelDao.get(javabotChannel.name)
        if (channel == null) {
            channel = Channel()
            channel.name = javabotChannel.name
            channel.logged = true
            channelDao.save(channel)
        }

        datastore.delete(logsDao.getQuery(Logs::class.java))
        datastore.delete(changeDao.getQuery(Change::class.java))
        javabot.get().start()
        enableAllOperations()
    }

    protected fun enableAllOperations() {
        val bot = this.javabot.get()
        bot.getAllOperations().keySet().forEach({ bot.enableOperation(it) })
    }

    protected fun disableAllOperations() {
        val bot = this.javabot.get()
        bot.getAllOperations().keySet().forEach({ bot.disableOperation(it) })
    }

    @BeforeMethod
    public fun clearMessages() {
        messages.get()
    }

    public fun getIrcBot(): PircBotX {
        return ircBot.get()
    }

    @AfterSuite
    @Throws(InterruptedException::class)
    public fun shutdown() {
        javabot.get().shutdown()
    }

    protected fun waitForEvent(event: AdminEvent, alias: String, timeout: Duration) {
        Awaitility.await(alias)
              .atMost(timeout)
              .pollInterval(5, TimeUnit.SECONDS)
              .until<Boolean> { done.contains(eventDao.find(event.id)?.state) }
    }

    protected fun registerIrcUser(nick: String, userName: String, host: String): User {
        val bob = userFactory.createUser(nick, userName, host)
        val info = NickServInfo(bob)
        info.registered = info.registered.minusDays(100)
        nickServDao.clear()
        nickServDao.save(info)
        return bob
    }

    companion object {
        public val TEST_NICK: String = "jbtestuser"
        public val TEST_USER_NICK: String = "botuser"
        public val TEST_BOT_NICK: String = "testjavabot"
        public val BOT_EMAIL: String = "test@example.com"
    }
}