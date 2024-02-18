package javabot

import com.antwerkz.sofia.Sofia
import com.google.common.collect.ImmutableMap.of
import com.google.inject.Inject
import com.jayway.awaitility.Duration
import java.util.concurrent.TimeUnit.SECONDS
import javabot.dao.FactoidDao
import javabot.dao.LogsDaoTest
import org.pircbotx.PircBotX
import org.pircbotx.User
import org.pircbotx.UserHostmask
import org.pircbotx.hooks.events.MessageEvent
import org.pircbotx.hooks.events.PrivateMessageEvent
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class IrcAdapterTest : BaseTest() {
    companion object {}

    @Inject lateinit var ircAdapter: IrcAdapter
    @Inject lateinit var factoidDao: FactoidDao
    val testIrcChannel: MockIrcChannel by lazy { MockIrcChannel(ircBot.get(), TEST_CHANNEL.name) }

    val testIrcUser: MockIrcUser by lazy { MockIrcUser(ircBot.get(), TEST_USER.nick) }

    val testIrcHostmask: MockUserHostmask by lazy { MockUserHostmask(ircBot.get(), TEST_USER.nick) }

    private val duration = Duration(10, SECONDS)

    @Test
    fun testOnMessage() {
        ircAdapter.onMessage(
            MessageEvent(
                ircBot.get(),
                testIrcChannel,
                testIrcChannel.name,
                testIrcHostmask,
                testIrcUser,
                "~dude",
                of()
            )
        )
        Assert.assertEquals(messages.get(duration)[0], Sofia.unhandledMessage(TEST_USER.nick))
    }

    @Test
    fun testOnPrivateMessage() {
        ircAdapter.onPrivateMessage(
            PrivateMessageEvent(ircBot.get(), testIrcHostmask, testIrcUser, "dude", of())
        )
        Assert.assertEquals(messages.get(duration)[0], Sofia.unhandledMessage(TEST_USER.nick))
    }

    @Test
    fun factoidLookup() {
        factoidDao.delete(TEST_USER.nick, "impact", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "impact", "<reply>ouch", LogsDaoTest.CHANNEL_NAME)
        ircAdapter.onMessage(
            MessageEvent(
                ircBot.get(),
                testIrcChannel,
                testIrcChannel.name,
                testIrcHostmask,
                testIrcUser,
                "~impact",
                of()
            )
        )
        Assert.assertEquals(messages.get(duration)[0], "ouch")
    }

    @Suppress("UNCHECKED_CAST")
    @DataProvider
    fun unicodeProvider(): Array<Array<Any>> =
        arrayOf(
            arrayOf("\u00c3foo", "Afoo"),
            arrayOf("\u00e3foo", "afoo"),
            arrayOf("\u00f1foo", "nfoo"),
            arrayOf("\u00f5foo", "ofoo")
        )
            as Array<Array<Any>>

    /** This is related to issue 259: unicode chars like ã, ñ, õ as ~a, ~n, ~o */
    @Test(dataProvider = "unicodeProvider")
    fun factoidLookupWithUnicode(input: String, conversion: String) {
        fun testWithValue(key: String) {
            factoidDao.delete(TEST_USER.nick, conversion, LogsDaoTest.CHANNEL_NAME)
            factoidDao.addFactoid(
                TEST_USER.nick,
                conversion,
                "<reply>something",
                LogsDaoTest.CHANNEL_NAME
            )
            ircAdapter.onMessage(
                MessageEvent(
                    ircBot.get(),
                    testIrcChannel,
                    testIrcChannel.name,
                    testIrcHostmask,
                    testIrcUser,
                    key,
                    of()
                )
            )
            Assert.assertEquals(messages.get(duration)[0], "something")
        }
        testWithValue("~$conversion")
        testWithValue(input)
    }

    @Test
    fun tell() {
        factoidDao.delete(TEST_USER.nick, "impact", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "impact", "<reply>ouch", LogsDaoTest.CHANNEL_NAME)
        ircAdapter.onMessage(
            MessageEvent(
                ircBot.get(),
                testIrcChannel,
                testIrcChannel.name,
                testIrcHostmask,
                testIrcUser,
                "~~ ${TARGET_USER.nick} impact",
                of()
            )
        )
        Assert.assertEquals(messages.get(duration)[0], "${TARGET_USER.nick}, ouch")
    }
}

class MockUserHostmask(
    bot: PircBotX,
    userNick: String,
    login: String = userNick,
    hostname: String = userNick
) : UserHostmask(bot, userNick, userNick, login, hostname) {}

class MockIrcUser(bot: PircBotX, userNick: String) :
    User(MockUserHostmask(bot, userNick, userNick, "localhost")) {}

class MockIrcChannel(bot: PircBotX, channelName: String) : org.pircbotx.Channel(bot, channelName) {}
