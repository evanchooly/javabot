package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import java.util.stream.Stream
import javabot.BaseTest
import javabot.operations.JSROperation
import javabot.service.JCPJSRLocator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@QuarkusTest
@Tag("operations")
class JSROperationTest : BaseTest() {
    private val locator: JCPJSRLocator by lazy { injector.getInstance(JCPJSRLocator::class.java) }
    private val operation: JSROperation by lazy { injector.getInstance(JSROperation::class.java) }

    @Test
    fun testLocatorConfig() {
        assertNotNull(locator)
    }

    @Test
    fun testJSROperations() {
        val response = operation.handleMessage(message("~jsr 220"))
        assertEquals(
            "'JSR 220: Enterprise JavaBeans 3.0' can be found at http://www.jcp" +
                ".org/en/jsr/detail?id=220",
            response[0].value,
        )
    }

    @Test
    fun testBadJSRRequest() {
        val response = operation.handleMessage(message("~jsr 2202213"))
        assertEquals(Sofia.jsrUnknown("2202213"), response[0].value)
    }

    companion object {
        @JvmStatic
        fun badCommands(): Stream<Arguments> =
            Stream.of(
                Arguments.of("~jsr", Sofia.jsrMissing()),
                Arguments.of("~jsr ", Sofia.jsrMissing()),
                Arguments.of("~jsr abc", Sofia.jsrInvalid("abc")),
            )
    }

    @ParameterizedTest
    @MethodSource("badCommands")
    fun testNullJSROperations(command: String, result: String) {
        val response = operation.handleMessage(message(command))
        assertEquals(result, response[0].value)
    }
}
