package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.dao.FactoidDao
import org.testng.Assert
import org.testng.annotations.Test
import javax.inject.Inject

@Test(groups = arrayOf("operations"))
public class ForgetFactoidOperationTest : BaseTest() {
    @Inject
    protected lateinit var factoidDao: FactoidDao
    @Inject
    protected lateinit var operation: ForgetFactoidOperation

    public fun forgetFactoid() {
        if (!factoidDao.hasFactoid("afky")) {
            factoidDao.addFactoid(testUser.nick, "afky", "test")
        }
        var response = operation.handleMessage(message("forget afky"))
        Assert.assertEquals(response[0].value, Sofia.factoidForgotten("afky", testUser.nick))
    }

    public fun nonexistentFactoid() {
        var response = operation.handleMessage(message("forget asdfghjkl"))
        Assert.assertEquals(response[0].value, Sofia.factoidDeleteUnknown("asdfghjkl", testUser.nick))
    }
}