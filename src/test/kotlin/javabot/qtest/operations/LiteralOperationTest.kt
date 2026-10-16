package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import java.util.Date
import javabot.BaseTest
import javabot.operations.LiteralOperation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@QuarkusTest
class LiteralOperationTest : BaseTest() {
    @Inject private lateinit var operation: LiteralOperation

    @Test
    @Tag("operations")
    fun testMissingFactoid() {
        val factoidName = "foo${Date().time}"
        var response = operation.handleMessage(message("~literal ${factoidName}"))
        assertEquals(Sofia.factoidUnknown(factoidName), response[0].value)
    }

    // TODO needs "existing factoid test"
}
