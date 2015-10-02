package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseMessagingTest
import javabot.BaseTest
import javabot.dao.FactoidDao
import org.testng.Assert
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.io.IOException
import java.util.Arrays
import javax.inject.Inject

@Test
public class GetFactoidOperationTest : BaseMessagingTest() {
    @Inject
    protected lateinit var factoidDao: FactoidDao

    @BeforeClass
    public fun createGets() {
        deleteFactoids()
        factoidDao.addFactoid(BaseTest.TEST_NICK, "api", "http://java.sun.com/javase/current/docs/api/index.html")
        factoidDao.addFactoid(BaseTest.TEST_NICK, "replyTest", "<reply>I'm a reply!")
        factoidDao.addFactoid(BaseTest.TEST_NICK, "stupid", "<reply>\$who, what you've just said is one of the most insanely idiotic " +
              "things I have ever heard. At no point in your rambling, incoherent response were you even close to anything that could be" +
              " considered a rational thought. Everyone in this room is now dumber for having listened to it. I award you no points, and" +
              " may God have mercy on your soul.")
        factoidDao.addFactoid(BaseTest.TEST_NICK, "seeTest", "<see>replyTest")
        factoidDao.addFactoid(BaseTest.TEST_NICK, "noReply", "I'm a reply!")
        factoidDao.addFactoid(BaseTest.TEST_NICK, "replace $1", "<reply>I replaced you $1")
        factoidDao.addFactoid(BaseTest.TEST_NICK, "camel $^", "<reply>$^")
        factoidDao.addFactoid(BaseTest.TEST_NICK, "url $+", "<reply>$+")
        factoidDao.addFactoid(BaseTest.TEST_NICK, "hey", "<reply>Hello, \$who")
        factoidDao.addFactoid(BaseTest.TEST_NICK, "coin", "<reply>(heads|tails)")
        factoidDao.addFactoid(BaseTest.TEST_NICK, "hug $1", "<action> hugs $1")
    }

    @AfterClass
    public fun deleteFactoids() {
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
            factoidDao.delete(BaseTest.TEST_NICK, key)
        }
    }

    @Throws(IOException::class)
    public fun straightGets() {
        testMessage("~api", getFoundMessage("api", "http://java.sun.com/javase/current/docs/api/index.html"))
        Assert.assertNotNull(factoidDao.getFactoid("api")?.lastUsed)
    }

    public fun replyGets() {
        testMessage("~replyTest", REPLY_VALUE)
    }

    public fun seeGets() {
        testMessage("~seeTest", REPLY_VALUE)
    }

    public fun seeReplyGets() {
        testMessage("~seeTest", REPLY_VALUE)
    }

    public fun parameterReplacement() {
        testMessage("~replace " + testUser, "I replaced you " + testUser)
        testMessage("~url what up doc", "what+up+doc")
        testMessage("~camel i should be camel case", "IShouldBeCamelCase")
    }

    public fun whoReplacement() {
        testMessage("~hey", "Hello, " + testUser)
    }

    public fun randomList() {
        testMessageList("~coin", Arrays.asList("heads", "tails"))
    }

    @Test(enabled = false)
    public fun guessFactoid() {
        testMessage("~bre", "I guess the factoid 'label line breaks' might be appropriate:")
    }

    public fun noGuess() {
        testMessage("~apiz", Sofia.unhandledMessage(testUser.nick))
    }

    public fun action() {
        testMessage("~hug " + BaseTest.TEST_NICK, " hugs " + BaseTest.TEST_NICK)
    }

    @Test
    public fun tell() {
        testMessage("~tell ${BaseTest.TEST_NICK} about hey", "Hello, ${BaseTest.TEST_NICK}" )
        testMessage("~tell ${BaseTest.TEST_NICK} about camel I am a test", BaseTest.TEST_NICK + ", IAmATest")
        testMessage("~tell ${BaseTest.TEST_NICK} about url I am a test", "${BaseTest.TEST_NICK}, I+am+a+test")
        testMessage("~tell ${BaseTest.TEST_NICK} about stupid", "${BaseTest.TEST_NICK}, what you've just said is one of the most " +
              "insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you even close to " +
              "anything that could be considered a rational thought. Everyone in this room is now dumber for having listened to it. I " +
              "award you no points, and may God have mercy on your soul." )
        Thread.sleep(6000)
        testMessage("~~ ${BaseTest.TEST_NICK} seeTest", "${BaseTest.TEST_NICK}, I'm a reply!")
        testMessage("~~ ${BaseTest.TEST_NICK} bobloblaw", Sofia.unhandledMessage(testUser.nick))
        testMessage("~~ ${BaseTest.TEST_NICK} api", "${BaseTest.TEST_NICK}, api is http://java.sun.com/javase/current/docs/api/index.html")
        validate("camel I am a test 2", "IAmATest2")
        testMessage("~~ ${BaseTest.TEST_NICK} url I am a test 2", "${BaseTest.TEST_NICK}, I+am+a+test+2")
        //    scanForResponse("~~ %s javadoc String", TEST_NICK), "[JDK: java.lang.String]");
        //    scanForResponse("~~ %s javadoc String", new IrcUser("jimbob")), "jimbob");
        testMessage("~~ ${BaseTest.TEST_NICK} stupid", "${BaseTest.TEST_NICK}, what you've just said is one of the most insanely idiotic" +
              " things I have ever heard. At no point in your rambling, incoherent response were you even close to anything that could " +
              "be considered a rational thought. Everyone in this room is now dumber for having listened to it. I award you no points, " +
              "and may God have mercy on your soul.")

        testMessage("~~${BaseTest.TEST_NICK} seeTest", "${BaseTest.TEST_NICK}, I'm a reply!")
        testMessage("~~${BaseTest.TEST_NICK} bobloblaw", Sofia.unhandledMessage(testUser.nick))

        testMessage("~~${BaseTest.TEST_NICK} api", "${BaseTest.TEST_NICK}, api is http://java.sun.com/javase/current/docs/api/index.html")
        testMessage("~~${BaseTest.TEST_NICK} camel I am a test 3", "${BaseTest.TEST_NICK}, IAmATest3")
        testMessage("~~${BaseTest.TEST_NICK} url I am a test 3", "${BaseTest.TEST_NICK}, I+am+a+test+3")
        validate("stupid", "what you've just said is one of the most insanely idiotic things I have ever heard. At no point in your " +
              "rambling, incoherent response were you even close to anything that could be considered a rational thought. Everyone in " +
              "this room is now dumber for having listened to it. I award you no points, and may God have mercy on your soul.")
    }

    @Test
    public fun longResponse() {
        factoidDao.addFactoid(BaseTest.TEST_NICK, "yalla $1",
              "<reply>$1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 $1 !111!!!!!one!!!\n")
        val messages = sendMessage( "~yalla I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated " +
              "spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long " +
              "repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a " +
              "really long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated spam" +
              " I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long " +
              "repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a " +
              "really long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated spam" +
              " I'm a really long repeated spam I'm a really long repeated spam I'm a really long repeated spam I'm a really long " +
              "repeated spam I'm a really long repeated spam I'm a really long repeated spam ")

        Assert.assertEquals(messages.size(), 1)
        Assert.assertTrue(messages.get(0).length() <= 510)
    }

    private fun validate(factoid: String, response: String) {
        testMessage("~~  ${BaseTest.TEST_NICK} ${factoid}", "${BaseTest.TEST_NICK}, ${response}")
    }

    companion object {
        private val REPLY_VALUE = "I'm a reply!"
    }
}