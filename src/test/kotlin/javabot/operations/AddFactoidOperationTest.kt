package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.Message
import javabot.dao.FactoidDao
import javabot.dao.LogsDaoTest
import javabot.model.JavabotUser
import javax.inject.Inject
import org.testng.Assert
import org.testng.Assert.assertEquals
import org.testng.annotations.BeforeMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test(groups = arrayOf("operations"))
class AddFactoidOperationTest
@Inject
constructor(
    val factoidDao: FactoidDao,
    val addFactoidOperation: AddFactoidOperation,
    val getFactoidOperation: GetFactoidOperation,
    val forgetFactoidOperation: ForgetFactoidOperation,
) : BaseTest() {

    companion object {
        val OK: String = Sofia.ok(TEST_USER_NICK.take(16))

        val TEST_NON_ADMIN_USER_NICK = "nonadminuser"
        val TEST_NON_ADMIN_USER =
            JavabotUser(TEST_NON_ADMIN_USER_NICK, TEST_NON_ADMIN_USER_NICK, "hostmask")
    }

    @BeforeMethod
    fun setUp() {
        factoidDao.delete(TEST_TARGET_NICK, "test", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "ping $1", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "what", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "what up", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "test pong", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "asdf", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "12345", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "replace", LogsDaoTest.CHANNEL_NAME)
    }

    fun factoidAdd() {
        var response = addFactoidOperation.handleMessage(message("~test pong is pong"))
        assertEquals(response[0].value, OK)
        response =
            addFactoidOperation.handleMessage(
                message(
                    "~ping \$1 is <action>sends some radar to \$1, awaits a" +
                        " response then forgets how long it took"
                )
            )
        assertEquals(response[0].value, OK)

        response = addFactoidOperation.handleMessage(message("~what? is a question"))
        assertEquals(response[0].value, OK)
        response = addFactoidOperation.handleMessage(message("~what up? is <see>what?"))
        assertEquals(response[0].value, OK)
    }

    @DataProvider
    fun replaceInput() =
        arrayOf(
            arrayOf("no"),
            arrayOf("No"),
            arrayOf("nO"),
            arrayOf("NO"), // let's be emphatic!
        )

    @Test(dataProvider = "replaceInput")
    fun replace(text: String) {
        var response = addFactoidOperation.handleMessage(message("~forget replace"))
        // we want to make sure that the factoid doesn't exist before anything else
        assertEquals(response.size, 0)

        response = addFactoidOperation.handleMessage(message("~replace is first entry"))
        assertEquals(response[0].value, OK)
        var factoid = factoidDao.getFactoid("replace")!!

        val updated = factoid.updated
        assertEquals(factoid.userName, TEST_USER.nick)

        response =
            addFactoidOperation.handleMessage(
                message("~$text, replace is <reply>second entry", user = TEST_NON_ADMIN_USER)
            )
        assertEquals(response[0].value, Sofia.ok(TEST_NON_ADMIN_USER.nick))

        factoid = factoidDao.getFactoid("replace")!!
        Assert.assertTrue(factoid.updated.isAfter(updated))
        assertEquals(factoid.userName, TEST_NON_ADMIN_USER.nick)

        response = getFactoidOperation.handleMessage(message("~replace"))
        assertEquals(response[0].value, "second entry")

        response = forgetFactoidOperation.handleMessage(message("~forget replace"))
        assertEquals(response[0].value, Sofia.factoidForgotten("replace", TEST_USER.nick))

        response =
            addFactoidOperation.handleMessage(message("~$text, replace is <reply>second entry"))
        assertEquals(response[0].value, Sofia.factoidUnknown("replace"))
    }

    @Test
    fun rejectDuplicate() {
        // clear out factoid first
        forgetFactoidOperation.handleMessage(message("~forget epesh", user = TEST_NON_ADMIN_USER))

        var response =
            addFactoidOperation.handleMessage(message("~epesh is cool", user = TEST_NON_ADMIN_USER))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, "OK, ${TEST_NON_ADMIN_USER.nick}.")
        val updated = factoidDao.getFactoid("epesh")!!.updated

        response =
            addFactoidOperation.handleMessage(
                message("~epesh is awesome", user = TEST_NON_ADMIN_USER)
            )
        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.factoidExists("epesh", TEST_NON_ADMIN_USER.nick))
        Assert.assertFalse(factoidDao.getFactoid("epesh")!!.updated.isAfter(updated))

        response = getFactoidOperation.handleMessage(message("~epesh", user = TEST_NON_ADMIN_USER))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, "${TEST_NON_ADMIN_USER.nick}, epesh is cool")

        response =
            forgetFactoidOperation.handleMessage(
                message("~forget epesh", user = TEST_NON_ADMIN_USER)
            )
        assertEquals(response.size, 1)
        assertEquals(response[0].value, Sofia.factoidForgotten("epesh", TEST_NON_ADMIN_USER.nick))
    }

    @Test(dependsOnMethods = arrayOf("factoidAdd"))
    fun duplicateAdd() {
        val message = "~test pong is pong"
        var response = addFactoidOperation.handleMessage(message(message))
        assertEquals(response[0].value, OK)
        response = addFactoidOperation.handleMessage(message(message))
        assertEquals(response[0].value, Sofia.factoidExists("test pong", TEST_USER.nick))
        forgetFactoidOperation.handleMessage(message("~forget test pong"))
    }

    fun blankValue() {
        val response = addFactoidOperation.handleMessage(message("~pong is"))
        assertEquals(response.size, 0)
    }

    fun addLog() {
        val response = addFactoidOperation.handleMessage(message("~12345 is 12345"))
        assertEquals(response[0].value, OK)
        Assert.assertTrue(
            changeDao.findLog(
                Sofia.factoidAdded(TEST_USER.nick, "12345", "12345", TEST_CHANNEL.name)
            )
        )
        forgetFactoidOperation.handleMessage(message("~forget 12345"))
    }

    fun parensFactoids() {
        val factoid = "should be the full (/hi there) factoid"
        var response = addFactoidOperation.handleMessage(message("~asdf is <reply>$factoid"))
        assertEquals(response[0].value, OK)
        response = getFactoidOperation.handleMessage(message("~asdf"))
        assertEquals(response[0].value, factoid)
    }

    fun privMessage() {
        bot.get()
            .processMessage(
                Message(TARGET_USER, System.currentTimeMillis().toString() + " is doh!")
            )
        assertEquals(messages.get()[0], Sofia.privmsgChange())
    }
}
