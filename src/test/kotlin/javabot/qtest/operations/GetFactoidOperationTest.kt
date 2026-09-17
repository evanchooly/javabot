package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import java.time.ZoneOffset
import java.util.Arrays
import java.util.stream.Stream
import javabot.BaseTest
import javabot.dao.FactoidDao
import javabot.operations.GetFactoidOperation
import javabot.qtest.dao.LogsDaoTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@QuarkusTest
class GetFactoidOperationTest : BaseTest() {
    private val factoidDao: FactoidDao by lazy { injector.getInstance(FactoidDao::class.java) }
    private val operation: GetFactoidOperation by lazy {
        injector.getInstance(GetFactoidOperation::class.java)
    }

    @BeforeEach
    fun createGets() {
        deleteFactoids()
        factoidDao.addFactoid(
            TEST_TARGET_NICK,
            "api",
            "http://java.sun.com/javase/current/docs/api/index.html",
            LogsDaoTest.CHANNEL_NAME,
        )
        factoidDao.addFactoid(
            TEST_TARGET_NICK,
            "replyTest",
            "<reply>I'm a reply!",
            LogsDaoTest.CHANNEL_NAME,
        )
        factoidDao.addFactoid(
            TEST_TARGET_NICK,
            "stupid",
            "<reply>\$who, what you've just said is one of the most insanely idiotic " +
                "things I have ever heard. At no point in your rambling, incoherent response were you even close to anything that could be" +
                " considered a rational thought. Everyone in this room is now dumber for having listened to it. I award you no points, and" +
                " may God have mercy on your soul.",
            LogsDaoTest.CHANNEL_NAME,
        )
        factoidDao.addFactoid(
            TEST_TARGET_NICK,
            "seeTest",
            "<see>replyTest",
            LogsDaoTest.CHANNEL_NAME,
        )
        factoidDao.addFactoid(TEST_TARGET_NICK, "noReply", "I'm a reply!", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(
            TEST_TARGET_NICK,
            "replace $1",
            "<reply>I replaced you $1",
            LogsDaoTest.CHANNEL_NAME,
        )
        factoidDao.addFactoid(TEST_TARGET_NICK, "camel $^", "<reply>$^", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_TARGET_NICK, "url $+", "<reply>$+", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(
            TEST_TARGET_NICK,
            "hey",
            "<reply>Hello, \$who",
            LogsDaoTest.CHANNEL_NAME,
        )
        factoidDao.addFactoid(
            TEST_TARGET_NICK,
            "coin",
            "<reply>(heads|tails)",
            LogsDaoTest.CHANNEL_NAME,
        )
        factoidDao.addFactoid(
            TEST_TARGET_NICK,
            "hug $1",
            "<action>hugs $1",
            LogsDaoTest.CHANNEL_NAME,
        )
    }

    @AfterEach
    fun deleteFactoids() {
        delete("api")
        delete("stupid")
        delete("replyTest")
        delete("seeTest")
        delete("noReply")
        delete("replace \$1")
        delete("url \$+")
        delete("camel \$C")
        delete("camel \$^")
        delete("camel \$+")
        delete("hey")
        delete("coin")
        delete("hug \$1")
        delete("yalla \$1")
    }

    private fun delete(key: String) {
        while (factoidDao.hasFactoid(key)) {
            factoidDao.delete(TEST_TARGET_NICK, key, LogsDaoTest.CHANNEL_NAME)
        }
    }

    @Test
    fun straightGets() {
        assertEquals(0, factoidDao.getFactoid("api")?.usage)
        val response = operation.handleMessage(message("~api"))
        assertEquals(
            getFoundMessage("api", "http://java.sun.com/javase/current/docs/api/index.html"),
            response[0].value,
        )
        assertNotNull(factoidDao.getFactoid("api")?.lastUsed)
        assertEquals(1, factoidDao.getFactoid("api")?.usage)
    }

    @Test
    fun dates() {
        factoidDao.delete(TEST_USER.nick, "dates", LogsDaoTest.CHANNEL_NAME)
        val dates =
            factoidDao.addFactoid(TEST_TARGET_NICK, "dates", "dates", LogsDaoTest.CHANNEL_NAME)
        operation.handleMessage(message("~dates"))

        val factoid = factoidDao.getFactoid("dates")!!
        assertEquals(
            dates.updated.toEpochSecond(ZoneOffset.UTC),
            factoid.updated.toEpochSecond(ZoneOffset.UTC),
        )
        assertTrue(factoid.lastUsed?.isAfter(dates.lastUsed) ?: false)
    }

    @Test
    fun replyGets() {
        val response = operation.handleMessage(message("~replyTest"))
        assertEquals(REPLY_VALUE, response[0].value)
    }

    @Test
    fun seeGets() {
        val response = operation.handleMessage(message("~seeTest"))
        assertEquals(REPLY_VALUE, response[0].value)
    }

    @Test
    fun seeReplyGets() {
        val response = operation.handleMessage(message("~seeTest"))
        assertEquals(REPLY_VALUE, response[0].value)
    }

    @Test
    fun parameterReplacement() {
        var response = operation.handleMessage(message("~replace $TEST_USER"))
        assertEquals("I replaced you " + TEST_USER, response[0].value)
        response = operation.handleMessage(message("~url what up doc"))
        assertEquals("what+up+doc", response[0].value)
        response = operation.handleMessage(message("~camel i should be camel case"))
        assertEquals("IShouldBeCamelCase", response[0].value)
    }

    @Test
    fun whoReplacement() {
        val response = operation.handleMessage(message("~hey"))
        assertEquals("Hello, " + TEST_USER, response[0].value)
    }

    @Test
    fun randomList() {
        val response = operation.handleMessage(message("~coin"))
        assertTrue(Arrays.asList("heads", "tails").contains(response[0].value))
    }

    @Test
    @Disabled
    fun guessFactoid() {
        val response = operation.handleMessage(message("~bre"))
        assertEquals(
            "I guess the factoid 'label line breaks' might be appropriate:",
            response[0].value,
        )
    }

    @Test
    fun noGuess() {
        val response = operation.handleMessage(message("~apiz"))
        assertEquals(0, response.size)
    }

    @Test
    fun action() {
        val response = operation.handleMessage(message("~hug $TEST_TARGET_NICK"))
        assertEquals("hugs $TEST_TARGET_NICK", response[0].value)
    }

    @Test
    fun actionWithoutTarget() {
        val response = operation.handleMessage(message("~hug "))
        assertEquals(Sofia.missingTarget("hug $1", TEST_USER_NICK), response[0].value)
    }

    companion object {
        /* unicode whitespaces as per http://www.fileformat.info/info/unicode/category/Zs/list.htm */
        @JvmStatic
        fun whitespaceProvider(): Stream<Arguments> =
            Stream.of(
                Arguments.of('\u0020'),
                Arguments.of('\u00a0'),
                Arguments.of('\u1680'),
                Arguments.of('\u2000'),
                Arguments.of('\u2001'),
                Arguments.of('\u2002'),
                Arguments.of('\u2003'),
                Arguments.of('\u2004'),
                Arguments.of('\u2005'),
                Arguments.of('\u2006'),
                Arguments.of('\u2007'),
                Arguments.of('\u2008'),
                Arguments.of('\u2009'),
                Arguments.of('\u200a'),
                Arguments.of('\u202f'),
                Arguments.of('\u205f'),
                Arguments.of('\u3000'),
            )

        private val REPLY_VALUE = "I'm a reply!"
    }

    /*
     * This test was changed to test all of the unicode whitespace characters, as per issue #177, trying to
     * find the problem. The problem remains unfound.
     */
    @ParameterizedTest
    @MethodSource("whitespaceProvider")
    fun testLeadingSpace(leader: Char) {
        var response =
            operation.handleMessage(message("~${leader}tell $TEST_TARGET_NICK about hey"))
        assertEquals("Hello, $TEST_TARGET_NICK", response[0].value)
        response = operation.handleMessage(message("~${leader}hey"))
        assertEquals("Hello, $TEST_USER_NICK", response[0].value)
        response = operation.handleMessage(message("~hey"))
        assertEquals("Hello, $TEST_USER_NICK", response[0].value)
        response = operation.handleMessage(message("~${leader}hey"))
        assertEquals("Hello, $TEST_USER_NICK", response[0].value)
    }

    @Test
    fun tell() {
        var response = operation.handleMessage(message("~tell $TEST_TARGET_NICK about hey"))
        assertEquals("Hello, $TEST_TARGET_NICK", response[0].value)
        response =
            operation.handleMessage(message("~tell $TEST_TARGET_NICK about camel I am a test"))
        assertEquals("$TEST_TARGET_NICK, IAmATest", response[0].value)
        response = operation.handleMessage(message("~tell $TEST_TARGET_NICK about url I am a test"))
        assertEquals("$TEST_TARGET_NICK, I+am+a+test", response[0].value)
        response = operation.handleMessage(message("~tell $TEST_TARGET_NICK about stupid"))
        assertEquals(
            "$TEST_TARGET_NICK, what you've just said is one of the most " +
                "insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you even close to " +
                "anything that could be considered a rational thought. Everyone in this room is now dumber for having listened to it. I " +
                "award you no points, and may God have mercy on your soul.",
            response[0].value,
        )
        response = operation.handleMessage(message("~~ $TEST_TARGET_NICK seeTest"))
        assertEquals("$TEST_TARGET_NICK, I'm a reply!", response[0].value)
        response = operation.handleMessage(message("~~ $TEST_TARGET_NICK bobloblaw"))
        assertEquals(0, response.size)
        response = operation.handleMessage(message("~~ $TEST_TARGET_NICK api"))
        assertEquals(
            "$TEST_TARGET_NICK, api is http://java.sun.com/javase/current/docs/api/index.html",
            response[0].value,
        )
        validate("camel I am a test 2", "IAmATest2")
        response = operation.handleMessage(message("~~ $TEST_TARGET_NICK url I am a test 2"))
        assertEquals("$TEST_TARGET_NICK, I+am+a+test+2", response[0].value)
        response = operation.handleMessage(message("~~ $TEST_TARGET_NICK stupid"))
        assertEquals(
            "$TEST_TARGET_NICK, what you've just said is one of the most insanely idiotic" +
                " things I have ever heard. At no point in your rambling, incoherent response were you even close to anything that could " +
                "be considered a rational thought. Everyone in this room is now dumber for having listened to it. I award you no points, " +
                "and may God have mercy on your soul.",
            response[0].value,
        )

        response = operation.handleMessage(message("~~$TEST_TARGET_NICK seeTest"))
        assertEquals("$TEST_TARGET_NICK, I'm a reply!", response[0].value)
        response = operation.handleMessage(message("~~$TEST_TARGET_NICK bobloblaw"))
        assertEquals(0, response.size)

        response = operation.handleMessage(message("~~$TEST_TARGET_NICK api"))
        assertEquals(
            "$TEST_TARGET_NICK, api is http://java.sun.com/javase/current/docs/api/index.html",
            response[0].value,
        )
        response = operation.handleMessage(message("~~$TEST_TARGET_NICK camel I am a test 3"))
        assertEquals("$TEST_TARGET_NICK, IAmATest3", response[0].value)
        response = operation.handleMessage(message("~~$TEST_TARGET_NICK url I am a test 3"))
        assertEquals("$TEST_TARGET_NICK, I+am+a+test+3", response[0].value)
        validate(
            "stupid",
            "what you've just said is one of the most insanely idiotic things I have ever heard. At no point in your " +
                "rambling, incoherent response were you even close to anything that could be considered a rational thought. Everyone in " +
                "this room is now dumber for having listened to it. I award you no points, and may God have mercy on your soul.",
        )
    }

    @Test
    fun longResponse() {
        factoidDao.addFactoid(
            TEST_TARGET_NICK,
            "yalla $1",
            "<reply>$1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 !111!!!!!one!!!\n",
            LogsDaoTest.CHANNEL_NAME,
        )
        val response =
            operation.handleMessage(
                message(
                    "~yalla I'm a really long repeated spam I'm a really long repeated spam I'm a " +
                        "really long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long " +
                        "repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated spam " +
                        "I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really " +
                        "long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated " +
                        "spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a " +
                        "really long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long " +
                        "repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated spam " +
                        "I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really " +
                        "long repeated spam I'm a really long repeated spam "
                )
            )

        assertEquals(1, response.size)
        assertTrue(response[0].value.length <= 510)
    }

    private fun validate(factoid: String, response: String) {
        val responses = operation.handleMessage(message("~~ $TEST_TARGET_NICK ${factoid}"))
        assertEquals("$TEST_TARGET_NICK, ${response}", responses[0].value)
    }

    private fun getFoundMessage(factoid: String, value: String): String {
        return "${TEST_USER}, ${factoid} is ${value}"
    }
}
