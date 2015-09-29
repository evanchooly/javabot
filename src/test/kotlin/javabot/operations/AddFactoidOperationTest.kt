package javabot.operations

import com.antwerkz.sofia.Sofia
import com.jayway.awaitility.Awaitility
import javabot.BaseMessagingTest
import javabot.BaseTest
import javabot.BotListener
import javabot.dao.FactoidDao
import javabot.dao.KarmaDao
import org.pircbotx.User
import org.pircbotx.hooks.events.PrivateMessageEvent
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.io.IOException
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Test(groups = arrayOf("operations"))
public class AddFactoidOperationTest : BaseMessagingTest() {
    @Inject
    protected lateinit var factoidDao: FactoidDao
    @Inject
    protected lateinit var listener: BotListener
    @Inject
    protected lateinit var karmaDao: KarmaDao

    @BeforeMethod
    public fun setUp() {
        factoidDao.delete(BaseTest.TEST_NICK, "test")
        factoidDao.delete(BaseTest.TEST_NICK, "ping $1")
        factoidDao.delete(BaseTest.TEST_NICK, "what")
        factoidDao.delete(BaseTest.TEST_NICK, "what up")
        factoidDao.delete(BaseTest.TEST_NICK, "test pong")
        factoidDao.delete(BaseTest.TEST_NICK, "asdf")
        factoidDao.delete(BaseTest.TEST_NICK, "12345")
        factoidDao.delete(BaseTest.TEST_NICK, "replace")
    }

    public fun factoidAdd() {
        testMessage("~test pong is pong", ok)
        testMessage("~ping $1 is <action>sends some radar to $1, awaits a response then forgets how long it took",
              ok)
        testMessage("~what? is a question", ok)
        testMessage("~what up? is <see>what?", ok)
    }

    public fun replace() {
        testMessage("~replace is first entry", ok)
        testMessage("~no, replace is <reply>second entry", Sofia.ok(testUser.nick))
        testMessage("~replace", "second entry")
        forgetFactoid("replace")
        testMessage("~no, replace is <reply>second entry", Sofia.ok(testUser.nick))
    }

    /**
     * This test relies on the KarmaOperation being in the operation set.
     */
    @Test
    public fun testAddOperationPriority() {
        val target = "foo is " + Date().time
        val karma = getKarma(userFactory.createUser(target, target, "localhost")) + 1
        testMessage("~$target++", Sofia.karmaOthersValue(target, karma, testUser.nick))
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
        karmaDao.delete(karmaDao.find(target)?.id)
    }

    @Test(dependsOnMethods = arrayOf("factoidAdd"))
    @Throws(IOException::class)
    public fun duplicateAdd() {
        val message = "~test pong is pong"
        testMessage(message, ok)
        testMessage(message, Sofia.factoidExists("test pong", testUser.nick))
        forgetFactoid("test pong")
    }

    public fun blankValue() {
        testMessage("~pong is", Sofia.unhandledMessage(testUser.nick))
    }

    public fun addLog() {
        testMessage("~12345 is 12345", ok)
        Assert.assertTrue(changeDao.findLog(Sofia.factoidAdded(testUser.nick, "12345", "12345")))
        forgetFactoid("12345")
    }

    public fun parensFactoids() {
        val factoid = "should be the full (/hi there) factoid"
        testMessage("~asdf is <reply>" + factoid, ok)
        testMessage("~asdf", factoid)
    }

    public fun privMessage() {
        listener.onPrivateMessage(PrivateMessageEvent(ircBot.get(), testUser, "privMessage is doh!"))
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until<Any> { !messages.isEmpty() }
        val messages = messages
        Assert.assertFalse(messages.isEmpty())
    }

    private fun getKarma(target: User): Int {
        return karmaDao.find(target.nick)?.value ?: 0
    }
}