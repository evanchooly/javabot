package javabot.qtest.operations

import io.quarkus.test.junit.QuarkusTest
import java.util.UUID
import javabot.BaseTest
import javabot.model.Logs
import javabot.model.Logs.Type
import javabot.operations.LogsOperation
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
class LogsOperationTest : BaseTest() {
    private val operation: LogsOperation by lazy { injector.getInstance(LogsOperation::class.java) }

    @BeforeEach
    @AfterEach
    fun clearLogs() {
        logsDao.deleteAllForChannel(TEST_CHANNEL.name)
    }

    @Test
    fun TEST_CHANNELLogs() {
        datastore.find(Logs::class.java).delete()
        // Add a known and unique message to the logs so we can validate that we are testing against
        // new data
        val uuid = UUID.randomUUID().toString()
        logsDao.logMessage(Type.MESSAGE, TEST_CHANNEL, TEST_USER, uuid)
        val list = operation.handleMessage(message("~logs"))
        assertFalse(list.isEmpty())
        assertTrue(list[0].value.contains(uuid))
    }

    @Test
    fun testNickSpecificLogsWhenNoLogsForNick() {
        // We generate unique user names so that existing data in the DB doesn't interfere with this
        // unit test
        val uuid = UUID.randomUUID().toString()
        val list = operation.handleMessage(message("~logs ${uuid}"))
        assertEquals(1, list.size)
        assertTrue(list[0].value.contains("No logs found for nick: $uuid"))
    }

    @Test
    fun testNickSpecificLogsWhenLogs() {
        val uuid = UUID.randomUUID().toString()
        val list = operation.handleMessage(message("~logs ${uuid}"))
        val listSize = list.size
        assertEquals(1, listSize)
        assertTrue(list[0].value.contains(uuid))
    }
}
