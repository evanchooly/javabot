package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.dao.FactoidDao
import javabot.dao.LogsDaoTest
import javabot.dao.LogsDaoTest.Companion
import org.testng.Assert
import org.testng.annotations.Test
import javax.inject.Inject

@Test(groups = arrayOf("operations")) class ForgetFactoidOperationTest : BaseTest() {
    @Inject
    protected lateinit var factoidDao: FactoidDao
    @Inject
    protected lateinit var operation: ForgetFactoidOperation

    fun forgetFactoid() {
        if (!factoidDao.hasFactoid("afky")) {
            factoidDao.addFactoid(testUser.nick, "afky", "test", LogsDaoTest.CHANNEL_NAME)
        }
        var response = operation.handleMessage(message("forget afky"))
        Assert.assertEquals(response[0].value, Sofia.factoidForgotten("afky", testUser.nick))
    }

    fun nonexistentFactoid() {
        var response = operation.handleMessage(message("forget asdfghjkl"))
        Assert.assertEquals(response[0].value, Sofia.factoidDeleteUnknown("asdfghjkl", testUser.nick))
    }
}