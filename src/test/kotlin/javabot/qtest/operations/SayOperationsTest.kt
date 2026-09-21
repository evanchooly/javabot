package javabot.qtest.operations

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import javabot.BaseTest
import javabot.mocks.MockIrcAdapter
import javabot.operations.SayOperation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@QuarkusTest
class SayOperationsTest : BaseTest() {
    @Inject private lateinit var operation: SayOperation

    @Test
    @Tag("operations")
    fun testSay() {
        val response = operation.handleMessage(message("~say MAGNIFICENT"))
        assertEquals(1, response.size)
        assertEquals("MAGNIFICENT", response[0].value)
    }

    @Test
    @Tag("operations")
    fun testSayNoOp() {
        val mockIrcAdapter = bot.adapter as MockIrcAdapter
        mockIrcAdapter.disableOperation("isOp")

        val response = operation.handleMessage(message("~say MAGNIFICENT"))
        assertEquals(0, response.size)
    }
}
