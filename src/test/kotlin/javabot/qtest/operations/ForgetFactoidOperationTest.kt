package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import javabot.BaseTest
import javabot.dao.FactoidDao
import javabot.operations.ForgetFactoidOperation
import javabot.qtest.dao.LogsDaoTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@QuarkusTest
class ForgetFactoidOperationTest : BaseTest() {

    private val factoidDao: FactoidDao by lazy { injector.getInstance(FactoidDao::class.java) }
    private val operation: ForgetFactoidOperation by lazy {
        injector.getInstance(ForgetFactoidOperation::class.java)
    }

    @Test
    fun forgetFactoid() {
        if (!factoidDao.hasFactoid("afky")) {
            factoidDao.addFactoid(TEST_USER.nick, "afky", "test", LogsDaoTest.CHANNEL_NAME)
        }
        var response = operation.handleMessage(message("~forget afky"))
        assertEquals(Sofia.factoidForgotten("afky", TEST_USER.nick), response[0].value)
    }

    @Test
    fun nonexistentFactoid() {
        var response = operation.handleMessage(message("~forget asdfghjkl"))
        assertEquals(Sofia.factoidDeleteUnknown("asdfghjkl", TEST_USER.nick), response[0].value)
    }
}
