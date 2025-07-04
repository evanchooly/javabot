package javabot.operations

import com.antwerkz.sofia.Sofia
import java.time.ZoneOffset
import java.util.Arrays
import javabot.BaseTest
import javabot.dao.FactoidDao
import javabot.dao.LogsDaoTest
import javax.inject.Inject
import org.testng.Assert
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test
class GetFactoidOperationTest : BaseTest() {
    @Inject private lateinit var factoidDao: FactoidDao
    @Inject private lateinit var operation: GetFactoidOperation

    @BeforeClass
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

    @AfterClass
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

    fun straightGets() {
        Assert.assertEquals(factoidDao.getFactoid("api")?.usage, 0)
        val response = operation.handleMessage(message("~api"))
        Assert.assertEquals(
            response[0].value,
            getFoundMessage("api", "http://java.sun.com/javase/current/docs/api/index.html"),
        )
        Assert.assertNotNull(factoidDao.getFactoid("api")?.lastUsed)
        Assert.assertEquals(factoidDao.getFactoid("api")?.usage, 1)
    }

    fun dates() {
        factoidDao.delete(TEST_USER.nick, "dates", LogsDaoTest.CHANNEL_NAME)
        val dates =
            factoidDao.addFactoid(TEST_TARGET_NICK, "dates", "dates", LogsDaoTest.CHANNEL_NAME)
        operation.handleMessage(message("~dates"))

        val factoid = factoidDao.getFactoid("dates")!!
        Assert.assertEquals(
            factoid.updated.toEpochSecond(ZoneOffset.UTC),
            dates.updated.toEpochSecond(ZoneOffset.UTC),
        )
        Assert.assertTrue(factoid.lastUsed?.isAfter(dates.lastUsed) ?: false)
    }

    fun replyGets() {
        val response = operation.handleMessage(message("~replyTest"))
        Assert.assertEquals(response[0].value, REPLY_VALUE)
    }

    fun seeGets() {
        val response = operation.handleMessage(message("~seeTest"))
        Assert.assertEquals(response[0].value, REPLY_VALUE)
    }

    fun seeReplyGets() {
        val response = operation.handleMessage(message("~seeTest"))
        Assert.assertEquals(response[0].value, REPLY_VALUE)
    }

    fun parameterReplacement() {
        var response = operation.handleMessage(message("~replace $TEST_USER"))
        Assert.assertEquals(response[0].value, "I replaced you " + TEST_USER)
        response = operation.handleMessage(message("~url what up doc"))
        Assert.assertEquals(response[0].value, "what+up+doc")
        response = operation.handleMessage(message("~camel i should be camel case"))
        Assert.assertEquals(response[0].value, "IShouldBeCamelCase")
    }

    fun whoReplacement() {
        val response = operation.handleMessage(message("~hey"))
        Assert.assertEquals(response[0].value, "Hello, " + TEST_USER)
    }

    fun randomList() {
        val response = operation.handleMessage(message("~coin"))
        Assert.assertTrue(Arrays.asList("heads", "tails").contains(response[0].value))
    }

    @Test(enabled = false)
    fun guessFactoid() {
        val response = operation.handleMessage(message("~bre"))
        Assert.assertEquals(
            response[0].value,
            "I guess the factoid 'label line breaks' might be appropriate:",
        )
    }

    fun noGuess() {
        val response = operation.handleMessage(message("~apiz"))
        Assert.assertEquals(response.size, 0)
    }

    fun action() {
        val response = operation.handleMessage(message("~hug $TEST_TARGET_NICK"))
        Assert.assertEquals(response[0].value, "hugs $TEST_TARGET_NICK")
    }

    fun actionWithoutTarget() {
        val response = operation.handleMessage(message("~hug "))
        Assert.assertEquals(response[0].value, Sofia.missingTarget("hug $1", TEST_USER_NICK))
    }

    /* unicode whitespaces as per http://www.fileformat.info/info/unicode/category/Zs/list.htm */
    @DataProvider
    fun whitespaceProvider(): Array<Array<Any>> {
        @Suppress("UNCHECKED_CAST")
        return arrayOf(
            arrayOf('\u0020'),
            arrayOf('\u00a0'),
            arrayOf('\u1680'),
            arrayOf('\u2000'),
            arrayOf('\u2001'),
            arrayOf('\u2002'),
            arrayOf('\u2003'),
            arrayOf('\u2004'),
            arrayOf('\u2005'),
            arrayOf('\u2006'),
            arrayOf('\u2007'),
            arrayOf('\u2008'),
            arrayOf('\u2009'),
            arrayOf('\u200a'),
            arrayOf('\u202f'),
            arrayOf('\u205f'),
            arrayOf('\u3000'),
        )
            as Array<Array<Any>>
    }

    /*
     * This test was changed to test all of the unicode whitespace characters, as per issue #177, trying to
     * find the problem. The problem remains unfound.
     */
    @Test(dataProvider = "whitespaceProvider")
    fun testLeadingSpace(leader: Char) {
        var response =
            operation.handleMessage(message("~${leader}tell $TEST_TARGET_NICK about hey"))
        Assert.assertEquals(response[0].value, "Hello, $TEST_TARGET_NICK")
        response = operation.handleMessage(message("~${leader}hey"))
        Assert.assertEquals(response[0].value, "Hello, $TEST_USER_NICK")
        response = operation.handleMessage(message("~hey"))
        Assert.assertEquals(response[0].value, "Hello, $TEST_USER_NICK")
        response = operation.handleMessage(message("~${leader}hey"))
        Assert.assertEquals(response[0].value, "Hello, $TEST_USER_NICK")
    }

    @Test
    fun tell() {
        var response = operation.handleMessage(message("~tell $TEST_TARGET_NICK about hey"))
        Assert.assertEquals(response[0].value, "Hello, $TEST_TARGET_NICK")
        response =
            operation.handleMessage(message("~tell $TEST_TARGET_NICK about camel I am a test"))
        Assert.assertEquals(response[0].value, "$TEST_TARGET_NICK, IAmATest")
        response = operation.handleMessage(message("~tell $TEST_TARGET_NICK about url I am a test"))
        Assert.assertEquals(response[0].value, "$TEST_TARGET_NICK, I+am+a+test")
        response = operation.handleMessage(message("~tell $TEST_TARGET_NICK about stupid"))
        Assert.assertEquals(
            response[0].value,
            "$TEST_TARGET_NICK, what you've just said is one of the most " +
                "insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you even close to " +
                "anything that could be considered a rational thought. Everyone in this room is now dumber for having listened to it. I " +
                "award you no points, and may God have mercy on your soul.",
        )
        response = operation.handleMessage(message("~~ $TEST_TARGET_NICK seeTest"))
        Assert.assertEquals(response[0].value, "$TEST_TARGET_NICK, I'm a reply!")
        response = operation.handleMessage(message("~~ $TEST_TARGET_NICK bobloblaw"))
        Assert.assertEquals(response.size, 0)
        response = operation.handleMessage(message("~~ $TEST_TARGET_NICK api"))
        Assert.assertEquals(
            response[0].value,
            "$TEST_TARGET_NICK, api is http://java.sun.com/javase/current/docs/api/index.html",
        )
        validate("camel I am a test 2", "IAmATest2")
        response = operation.handleMessage(message("~~ $TEST_TARGET_NICK url I am a test 2"))
        Assert.assertEquals(response[0].value, "$TEST_TARGET_NICK, I+am+a+test+2")
        response = operation.handleMessage(message("~~ $TEST_TARGET_NICK stupid"))
        Assert.assertEquals(
            response[0].value,
            "$TEST_TARGET_NICK, what you've just said is one of the most insanely idiotic" +
                " things I have ever heard. At no point in your rambling, incoherent response were you even close to anything that could " +
                "be considered a rational thought. Everyone in this room is now dumber for having listened to it. I award you no points, " +
                "and may God have mercy on your soul.",
        )

        response = operation.handleMessage(message("~~$TEST_TARGET_NICK seeTest"))
        Assert.assertEquals(response[0].value, "$TEST_TARGET_NICK, I'm a reply!")
        response = operation.handleMessage(message("~~$TEST_TARGET_NICK bobloblaw"))
        Assert.assertEquals(response.size, 0)

        response = operation.handleMessage(message("~~$TEST_TARGET_NICK api"))
        Assert.assertEquals(
            response[0].value,
            "$TEST_TARGET_NICK, api is http://java.sun.com/javase/current/docs/api/index.html",
        )
        response = operation.handleMessage(message("~~$TEST_TARGET_NICK camel I am a test 3"))
        Assert.assertEquals(response[0].value, "$TEST_TARGET_NICK, IAmATest3")
        response = operation.handleMessage(message("~~$TEST_TARGET_NICK url I am a test 3"))
        Assert.assertEquals(response[0].value, "$TEST_TARGET_NICK, I+am+a+test+3")
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

        Assert.assertEquals(response.size, 1)
        Assert.assertTrue(response[0].value.length <= 510)
    }

    private fun validate(factoid: String, response: String) {
        val responses = operation.handleMessage(message("~~ $TEST_TARGET_NICK ${factoid}"))
        Assert.assertEquals(responses[0].value, "$TEST_TARGET_NICK, ${response}")
    }

    private fun getFoundMessage(factoid: String, value: String): String {
        return "${TEST_USER}, ${factoid} is ${value}"
    }

    companion object {
        private val REPLY_VALUE = "I'm a reply!"
    }
}
