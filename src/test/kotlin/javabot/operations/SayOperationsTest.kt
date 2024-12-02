package javabot.operations

import jakarta.inject.Inject
import javabot.BaseTest
import javabot.MockIrcAdapter
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

@Test(groups = arrayOf("operations"))
class SayOperationsTest : BaseTest() {
    @Inject private lateinit var operation: SayOperation

    fun testSay() {
        val response = operation.handleMessage(message("~say MAGNIFICENT"))
        assertEquals(response.size, 1)
        assertEquals(response[0].value, "MAGNIFICENT")
    }

    fun testSayNoOp() {
        val mockIrcAdapter = bot.get().adapter as MockIrcAdapter
        mockIrcAdapter.disableOperation("isOp")

        val response = operation.handleMessage(message("~say MAGNIFICENT"))
        assertEquals(response.size, 0)
    }
}
