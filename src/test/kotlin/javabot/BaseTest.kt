package javabot

import com.jayway.awaitility.Awaitility
import com.jayway.awaitility.Duration
import dev.morphia.Datastore
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.inject.Provider
import java.util.EnumSet
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChangeDao
import javabot.dao.ChannelDao
import javabot.dao.LogsDao
import javabot.dao.NickServDao
import javabot.model.Admin
import javabot.model.AdminEvent
import javabot.model.ApiEvent
import javabot.model.Change
import javabot.model.Channel
import javabot.model.EventInjector
import javabot.model.JavabotUser
import javabot.model.Logs
import javabot.model.NickServInfo
import javabot.model.State
import javabot.model.javadoc.JavadocApi
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.pircbotx.PircBotX
import org.slf4j.LoggerFactory

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseTest {

    companion object {
        val TEST_TARGET_NICK: String = "jbtestuser"
        val TEST_USER_NICK: String = "botuser"
        val TEST_BOT_NICK: String = "testjavabot"
        val BOT_EMAIL: String = "test@example.com"
        val DONE: EnumSet<State> = EnumSet.of(State.COMPLETED, State.FAILED)
        val TARGET_USER = JavabotUser(TEST_TARGET_NICK, TEST_TARGET_NICK, "hostmask")
        val TEST_USER = JavabotUser(TEST_USER_NICK, TEST_USER_NICK, "hostmask")
        val TEST_CHANNEL = Channel("#jbunittest")
        private val LOG = LoggerFactory.getLogger(BaseTest::class.java)
    }

    @Inject protected lateinit var datastore: Datastore
    @Inject private lateinit var config: JavabotConfig
    @Inject protected lateinit var apiDao: ApiDao
    @Inject protected lateinit var channelDao: ChannelDao
    @Inject protected lateinit var logsDao: LogsDao
    @Inject protected lateinit var adminDao: AdminDao
    @Inject protected lateinit var changeDao: ChangeDao
    @Inject protected lateinit var bot: TestJavabot
    @Inject protected lateinit var ircBot: Provider<PircBotX>
    @Inject protected lateinit var messages: Messages
    @Inject protected lateinit var eventInjector: EventInjector

    @BeforeEach
    fun setup() {
        LOG.debug("setting up test")
        messages.clear()
        val admin =
            adminDao.getAdminByEmailAddress(BOT_EMAIL)
                ?: Admin(TEST_USER.nick, BOT_EMAIL, TEST_USER.hostmask, true)
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
        bot.start()
    }

    protected fun enableAllOperations() {
        bot.getAllOperations().keys.forEach { bot.enableOperation(it) }
    }

    protected fun disableAllOperations() {
        bot.getAllOperations().keys.forEach { bot.disableOperation(it) }
    }

    protected fun waitForEvent(
        event: AdminEvent,
        alias: String,
        timeout: Duration = Duration(2, SECONDS),
    ) {
        Awaitility.await(alias).atMost(timeout).pollInterval(100, MILLISECONDS).until<Boolean> {
            DONE.contains(event.state)
        }
    }

    protected fun message(
        value: String,
        start: String = "~",
        user: JavabotUser = TEST_USER,
    ): Message {
        return Message.extractContentFromMessage(bot, TEST_CHANNEL, user, start, bot.nick, value)
    }

    protected fun privateMessage(value: String, user: JavabotUser = TEST_USER): Message {
        return Message(user, value)
    }

    protected fun scanForResponse(messages: List<Message>, target: String) {
        var found = false
        for (response in messages) {
            found = found or response.value.contains(target)
        }
        assertTrue(
            found,
            java.lang.String.format(
                "Did not find \n'%s' in \n'%s'",
                target,
                messages.joinToString("\n") { it.value },
            ),
        )
    }

    protected fun loadApi(
        apiName: String,
        groupId: String = "",
        artifactId: String = "",
        version: String,
    ): JavadocApi {
        var api = apiDao.find(apiName)
        if (api == null) {
            LOG.info("$apiName API not found.  Generating now.")
            api = JavadocApi(config, apiName, groupId, artifactId, version)
            apiDao.save(api)
            val event = ApiEvent.add(TEST_USER.nick, api)
            eventInjector.inject(event)
            event.handle()
            messages.clear()
            LOG.info("$apiName finished.")
        }

        return api
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
