package javabot.operations

import javabot.BaseTest
import javabot.model.Logs
import javabot.model.Logs.Type
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.util.UUID
import javax.inject.Inject

public class LogsOperationTest : BaseTest() {
    @Inject
    private lateinit var operation: LogsOperation

    @BeforeMethod
    @AfterMethod
    fun clearLogs() {
        logsDao.deleteAllForChannel(testChannel.name)
    }

    @Test
    @Throws(Exception::class)
    public fun testChannelLogs() {
        val query = datastore.createQuery(Logs::class.java)
        datastore.delete(query)
        // Add a known and unique message to the logs so we can validate that we are testing against new data
        val uuid = UUID.randomUUID().toString()
        logsDao.logMessage(Type.MESSAGE, testChannel, testUser, uuid)
        val list = operation.handleMessage(message("logs"))
        Assert.assertFalse(list.isEmpty())
        Assert.assertTrue(list[0].value.contains(uuid))
    }

    @Test
    @Throws(Exception::class)
    public fun testNickSpecificLogsWhenNoLogsForNick() {
        // We generate unique user names so that existing data in the DB doesn't interfere with this unit test
        val uuid = UUID.randomUUID().toString()
        val list = operation.handleMessage(message("logs ${uuid}"))
        Assert.assertEquals(list.size, 1)
        Assert.assertTrue(list[0].value.contains("No logs found for nick: $uuid"))
    }

    @Test
    @Throws(Exception::class)
    public fun testNickSpecificLogsWhenLogs() {
        val uuid = UUID.randomUUID().toString()
        val list = operation.handleMessage(message("logs ${uuid}"))
        val listSize = list.size
        Assert.assertEquals(listSize, 1)
        Assert.assertTrue(list[0].value.contains(uuid))
    }
}
