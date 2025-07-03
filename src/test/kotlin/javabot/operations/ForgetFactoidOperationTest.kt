package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.dao.FactoidDao
import javabot.dao.LogsDaoTest
import javax.inject.Inject
import org.testng.Assert
import org.testng.annotations.Test

@Test
class ForgetFactoidOperationTest : BaseTest() {

    @Inject private lateinit var factoidDao: FactoidDao
    @Inject private lateinit var operation: ForgetFactoidOperation

    fun forgetFactoid() {
        if (!factoidDao.hasFactoid("afky")) {
            factoidDao.addFactoid(TEST_USER.nick, "afky", "test", LogsDaoTest.CHANNEL_NAME)
        }
        var response = operation.handleMessage(message("~forget afky"))
        Assert.assertEquals(response[0].value, Sofia.factoidForgotten("afky", TEST_USER.nick))
    }

    fun nonexistentFactoid() {
        var response = operation.handleMessage(message("~forget asdfghjkl"))
        Assert.assertEquals(
            response[0].value,
            Sofia.factoidDeleteUnknown("asdfghjkl", TEST_USER.nick),
        )
    }
}
