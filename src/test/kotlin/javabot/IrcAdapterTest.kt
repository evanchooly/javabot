package javabot

import com.antwerkz.sofia.Sofia
import com.google.common.collect.ImmutableMap.of
import com.jayway.awaitility.Duration
import io.quarkus.test.junit.QuarkusTest
import java.util.concurrent.TimeUnit.SECONDS
import java.util.stream.Stream
import javabot.dao.FactoidDao
import javabot.mocks.MockIrcChannel
import javabot.mocks.MockIrcUser
import javabot.mocks.MockUserHostmask
import javabot.qtest.dao.LogsDaoTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pircbotx.hooks.events.MessageEvent
import org.pircbotx.hooks.events.PrivateMessageEvent

@QuarkusTest
class IrcAdapterTest : BaseTest() {
    private val ircAdapter: IrcAdapter by lazy { injector.getInstance(IrcAdapter::class.java) }
    private val factoidDao: FactoidDao by lazy { injector.getInstance(FactoidDao::class.java) }
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
                of(),
            )
        )
        assertEquals(Sofia.unhandledMessage(TEST_USER.nick), messages.get(duration)[0])
    }

    @Test
    fun testOnPrivateMessage() {
        ircAdapter.onPrivateMessage(
            PrivateMessageEvent(ircBot.get(), testIrcHostmask, testIrcUser, "dude", of())
        )
        assertEquals(Sofia.unhandledMessage(TEST_USER.nick), messages.get(duration)[0])
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
                of(),
            )
        )
        assertEquals("ouch", messages.get(duration)[0])
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun unicodeProvider(): Stream<Arguments> =
            Stream.of(
                Arguments.of("\u00c3foo", "Afoo"),
                Arguments.of("\u00e3foo", "afoo"),
                Arguments.of("\u00f1foo", "nfoo"),
                Arguments.of("\u00f5foo", "ofoo"),
            )
    }

    /** This is related to issue 259: unicode chars like ã, ñ, õ as ~a, ~n, ~o */
    @ParameterizedTest
    @MethodSource("unicodeProvider")
    fun factoidLookupWithUnicode(input: String, conversion: String) {
        fun testWithValue(key: String) {
            factoidDao.delete(TEST_USER.nick, conversion, LogsDaoTest.CHANNEL_NAME)
            factoidDao.addFactoid(
                TEST_USER.nick,
                conversion,
                "<reply>something",
                LogsDaoTest.CHANNEL_NAME,
            )
            ircAdapter.onMessage(
                MessageEvent(
                    ircBot.get(),
                    testIrcChannel,
                    testIrcChannel.name,
                    testIrcHostmask,
                    testIrcUser,
                    key,
                    of(),
                )
            )
            assertEquals("something", messages.get(duration)[0])
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
                of(),
            )
        )
        assertEquals("${TARGET_USER.nick}, ouch", messages.get(duration)[0])
    }
}
