package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import javabot.BaseTest
import javabot.operations.SeenOperation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@QuarkusTest
class SeenOperationTest : BaseTest() {
    private val operation: SeenOperation by lazy { injector.getInstance(SeenOperation::class.java) }

    @Test
    fun seen() {
        var response = operation.handleMessage(message("~seen jimmyjimjim"))
        assertEquals(Sofia.seenUnknown(TEST_USER.nick, "jimmyjimjim"), response[0].value)
    }
}
