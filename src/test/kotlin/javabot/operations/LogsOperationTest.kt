package javabot.operations

import java.util.UUID
import javabot.BaseTest
import javabot.model.Logs
import javabot.model.Logs.Type
import javax.inject.Inject
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class LogsOperationTest : BaseTest() {
    @Inject private lateinit var operation: LogsOperation

    @BeforeMethod
    @AfterMethod
    fun clearLogs() {
        logsDao.deleteAllForChannel(TEST_CHANNEL.name)
    }

    @Test
    @Throws(Exception::class)
    fun TEST_CHANNELLogs() {
        datastore.find(Logs::class.java).delete()
        // Add a known and unique message to the logs so we can validate that we are testing against
        // new data
        val uuid = UUID.randomUUID().toString()
        logsDao.logMessage(Type.MESSAGE, TEST_CHANNEL, TEST_USER, uuid)
        val list = operation.handleMessage(message("~logs"))
        Assert.assertFalse(list.isEmpty())
        Assert.assertTrue(list[0].value.contains(uuid))
    }

    @Test
    @Throws(Exception::class)
    fun testNickSpecificLogsWhenNoLogsForNick() {
        // We generate unique user names so that existing data in the DB doesn't interfere with this
        // unit test
        val uuid = UUID.randomUUID().toString()
        val list = operation.handleMessage(message("~logs ${uuid}"))
        Assert.assertEquals(list.size, 1)
        Assert.assertTrue(list[0].value.contains("No logs found for nick: $uuid"))
    }

    @Test
    @Throws(Exception::class)
    fun testNickSpecificLogsWhenLogs() {
        val uuid = UUID.randomUUID().toString()
        val list = operation.handleMessage(message("~logs ${uuid}"))
        val listSize = list.size
        Assert.assertEquals(listSize, 1)
        Assert.assertTrue(list[0].value.contains(uuid))
    }
}
