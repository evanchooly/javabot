package javabot.operations

import javabot.BaseTest
import javabot.dao.FactoidDao
import javabot.dao.LogsDaoTest
import org.testng.Assert
import org.testng.annotations.Test
import javax.inject.Inject

@Test(enabled = false) class ShunOperationTest : BaseTest() {
    @Inject
    private lateinit var operation: ShunOperation
    @Inject
    private lateinit var getFactoidOperation: GetFactoidOperation
    @Inject
    private lateinit var factoidDao: FactoidDao


    @Throws(InterruptedException::class) fun shunMe() {
        factoidDao.delete(TEST_USER.nick, "shunHey", LogsDaoTest.CHANNEL_NAME)
        try {
            factoidDao.addFactoid(TEST_USER.nick, "shunHey", "<reply>shunHey", LogsDaoTest.CHANNEL_NAME)
            var response = operation.handleMessage(message("~shun ${TEST_USER} 5"))
            Assert.assertEquals(response[0].value, "${TEST_USER} is shunned until")
            response = operation.handleMessage(message("~shunHey"))
            Assert.assertTrue(response.isEmpty())
            Thread.sleep(5000)
            response = getFactoidOperation.handleMessage(message("~shunHey"))
            Assert.assertEquals(response[0].value, "shunHey")
        } finally {
            factoidDao.delete(TEST_USER.nick, "shunHey", LogsDaoTest.CHANNEL_NAME)
        }
    }
}
