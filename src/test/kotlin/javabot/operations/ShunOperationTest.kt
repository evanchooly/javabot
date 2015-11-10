package javabot.operations

import javabot.BaseMessagingTest
import javabot.BaseTest
import javabot.dao.FactoidDao
import org.testng.Assert
import org.testng.annotations.Test
import javax.inject.Inject

@Test(enabled = false)
public class ShunOperationTest : BaseTest() {
    @Inject
    private lateinit var operation: ShunOperation
    @Inject
    private lateinit var getFactoidOperation: GetFactoidOperation
    @Inject
    private lateinit var factoidDao: FactoidDao


    @Throws(InterruptedException::class)
    public fun shunMe() {
        factoidDao.delete(testUser.nick, "shunHey")
        try {
            factoidDao.addFactoid(testUser.nick, "shunHey", "<reply>shunHey")
            var response = operation.handleMessage(message("shun ${testUser} 5"))
            Assert.assertEquals(response[0].value, "${testUser} is shunned until")
            response = operation.handleMessage(message("shunHey"))
            Assert.assertTrue(response.isEmpty())
            Thread.sleep(5000)
            response = getFactoidOperation.handleMessage(message("shunHey"))
            Assert.assertEquals(response[0].value, "shunHey")
        } finally {
            factoidDao.delete(testUser.nick, "shunHey")
        }
    }
}
