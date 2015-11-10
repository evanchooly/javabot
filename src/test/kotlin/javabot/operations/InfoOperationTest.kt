package javabot.operations

import com.google.inject.Inject
import javabot.BaseTest
import javabot.dao.FactoidDao
import org.testng.Assert
import org.testng.annotations.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

public class InfoOperationTest : BaseTest() {
    @Inject
    protected lateinit var factoidDao: FactoidDao
    @Inject
    protected lateinit var operation: InfoOperation

    @Test
    public fun info() {
        val key = "whatwhat"
        val value = "ah, yeah"
        val user = "test"
        try {
            val now = LocalDateTime.now()
            var factoid = factoidDao.addFactoid(user, key, value)
            factoid.updated = now
            factoidDao.save(factoid)
            val format = now.format(DateTimeFormatter.ofPattern(InfoOperation.INFO_DATE_FORMAT))
            var response = operation.handleMessage(message("info " + key))
            Assert.assertEquals(response[0].value, "${key} was added by: ${user} on ${format} and has a literal value of: ${value}")
        } finally {
            var factoid = factoidDao.getFactoid(key)
            if (factoid != null) {
                factoidDao.delete(factoid)
            }
        }
    }
}
