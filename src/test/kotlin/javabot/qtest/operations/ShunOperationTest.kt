package javabot.qtest.operations

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import javabot.BaseTest
import javabot.dao.FactoidDao
import javabot.operations.GetFactoidOperation
import javabot.operations.ShunOperation
import javabot.qtest.dao.LogsDaoTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@QuarkusTest
class ShunOperationTest : BaseTest() {
    @Inject private lateinit var operation: ShunOperation
    @Inject private lateinit var getFactoidOperation: GetFactoidOperation
    @Inject private lateinit var factoidDao: FactoidDao

    @Test
    @Disabled
    fun shunMe() {
        factoidDao.delete(TEST_USER.nick, "shunHey", LogsDaoTest.CHANNEL_NAME)
        try {
            factoidDao.addFactoid(
                TEST_USER.nick,
                "shunHey",
                "<reply>shunHey",
                LogsDaoTest.CHANNEL_NAME,
            )
            var response = operation.handleMessage(message("~shun ${TEST_USER} 5"))
            assertEquals("${TEST_USER} is shunned until", response[0].value)
            response = operation.handleMessage(message("~shunHey"))
            assertTrue(response.isEmpty())
            Thread.sleep(5000)
            response = getFactoidOperation.handleMessage(message("~shunHey"))
            assertEquals("shunHey", response[0].value)
        } finally {
            factoidDao.delete(TEST_USER.nick, "shunHey", LogsDaoTest.CHANNEL_NAME)
        }
    }
}
