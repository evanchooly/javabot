package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.dao.FactoidDao
import javabot.dao.LogsDaoTest
import javabot.dao.LogsDaoTest.Companion
import org.testng.Assert
import org.testng.annotations.Test
import javax.inject.Inject

@Test(groups = arrayOf("operations"))
class ForgetFactoidOperationTest @Inject constructor(var factoidDao: FactoidDao, var operation: ForgetFactoidOperation) : BaseTest() {

    fun forgetFactoid() {
        if (!factoidDao.hasFactoid("afky")) {
            factoidDao.addFactoid(TEST_USER.nick, "afky", "test", LogsDaoTest.CHANNEL_NAME)
        }
        var response = operation.handleMessage(message("~forget afky"))
        Assert.assertEquals(response[0].value, Sofia.factoidForgotten("afky", TEST_USER.nick))
    }

    fun nonexistentFactoid() {
        var response = operation.handleMessage(message("~forget asdfghjkl"))
        Assert.assertEquals(response[0].value, Sofia.factoidDeleteUnknown("asdfghjkl", TEST_USER.nick))
    }
}