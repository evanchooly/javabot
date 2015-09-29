package javabot.operations

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.google.inject.Inject
import javabot.BaseMessagingTest
import javabot.dao.FactoidDao
import javabot.model.Factoid
import org.testng.annotations.Test

public class InfoOperationTest : BaseMessagingTest() {
    @Inject
    protected lateinit var factoidDao: FactoidDao

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
            testMessage("~info " + key, "${key} was added by: ${user} on ${format} and has a literal value of: ${value}")
        } finally {
            var factoid = factoidDao.getFactoid(key)
            if (factoid != null) {
                factoidDao.delete(factoid)
            }
        }
    }
}
