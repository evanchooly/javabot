package javabot.qtest.operations

import io.quarkus.test.junit.QuarkusTest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javabot.BaseTest
import javabot.dao.FactoidDao
import javabot.operations.GetFactoidOperation
import javabot.operations.InfoOperation
import javabot.qtest.dao.LogsDaoTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@QuarkusTest
class InfoOperationTest : BaseTest() {
    private val factoidDao: FactoidDao by lazy { injector.getInstance(FactoidDao::class.java) }
    private val operation: InfoOperation by lazy { injector.getInstance(InfoOperation::class.java) }
    private val factoidOperation: GetFactoidOperation by lazy {
        injector.getInstance(GetFactoidOperation::class.java)
    }

    @Test
    fun info() {
        val key = "whatwhat"
        val value = "ah, yeah"
        val user = "test"
        try {
            val now = LocalDateTime.of(2014, 3, 23, 21, 12)
            factoidDao.addFactoid(user, key, value, LogsDaoTest.CHANNEL_NAME, now)
            val format = now.format(DateTimeFormatter.ofPattern(InfoOperation.INFO_DATE_FORMAT))
            var response = operation.handleMessage(message("~info " + key))
            assertEquals(
                "${key} was added by: ${user} on ${format} and has a literal value of: ${value}",
                response[0].value,
            )
            response = factoidOperation.handleMessage(message("~whatwhat"))
            assertEquals("botuser, whatwhat is ah, yeah", response[0].value)
            response = operation.handleMessage(message("~info " + key))
            assertEquals(
                "${key} was added by: ${user} on ${format} and has a literal value of: ${value}",
                response[0].value,
            )
        } finally {
            var factoid = factoidDao.getFactoid(key)
            if (factoid != null) {
                factoidDao.delete(factoid)
            }
        }
    }
}
