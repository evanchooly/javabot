package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseMessagingTest
import javabot.dao.FactoidDao
import org.testng.annotations.Test
import javax.inject.Inject

@Test(groups = arrayOf("operations"))
public class ForgetFactoidOperationTest : BaseMessagingTest() {
    @Inject
    protected lateinit var factoidDao: FactoidDao

    public fun forgetFactoid() {
        if (!factoidDao.hasFactoid("afky")) {
            factoidDao.addFactoid(testUser.nick, "afky", "test")
        }
        testMessage("~forget afky", Sofia.factoidForgotten("afky", testUser.nick))
    }

    public fun nonexistentFactoid() {
        testMessage("~forget asdfghjkl", Sofia.factoidDeleteUnknown("asdfghjkl", testUser.nick))
    }
}