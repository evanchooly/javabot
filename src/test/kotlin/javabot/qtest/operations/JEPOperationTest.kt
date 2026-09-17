package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import java.util.stream.Stream
import javabot.BaseTest
import javabot.operations.JEPOperation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@QuarkusTest
@Tag("operations")
class JEPOperationTest : BaseTest() {
    private val operation: JEPOperation by lazy { injector.getInstance(JEPOperation::class.java) }

    @Test
    fun testJEPOperations() {
        val response = operation.handleMessage(message("~jep 220"))
        assertEquals(
            "'JEP 220: Modular Run-Time Images' can be found at " +
                "http://openjdk.java.net/jeps/220",
            response[0].value,
        )
    }

    @Test
    fun testBadJEPRequest() {
        val response = operation.handleMessage(message("~jep 2202213"))
        assertEquals(Sofia.jepInvalid("2202213"), response[0].value)
    }

    companion object {
        @JvmStatic
        fun badCommands(): Stream<Arguments> =
            Stream.of(
                Arguments.of("~jep", Sofia.jepMissing()),
                Arguments.of("~jep ", Sofia.jepMissing()),
                Arguments.of("~jep abc", Sofia.jepInvalid("abc")),
            )
    }

    @ParameterizedTest
    @MethodSource("badCommands")
    fun testNullJEPOperations(command: String, result: String) {
        val response = operation.handleMessage(message(command))
        assertEquals(result, response[0].value)
    }
}
