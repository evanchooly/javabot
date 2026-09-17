package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import java.util.stream.Stream
import javabot.BaseTest
import javabot.operations.RFCOperation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@QuarkusTest
@Tag("operations")
class RFCOperationTest : BaseTest() {
    private val operation: RFCOperation by lazy { injector.getInstance(RFCOperation::class.java) }

    companion object {
        @JvmStatic
        fun getRfcsCommandAndExpectedMessages(): Stream<Arguments> =
            Stream.of(
                Arguments.of("~rfc abd", Sofia.rfcInvalid("abd")),
                Arguments.of("~rfc", Sofia.rfcMissing()),
                Arguments.of(
                    "~rfc 2616",
                    Sofia.rfcSucceed(
                        "https://tools.ietf.org/html/rfc2616",
                        "Hypertext Transfer Protocol -- HTTP/1.1",
                    ),
                ),
                Arguments.of("~rfc 8675309", Sofia.rfcFail("8675309")),
                Arguments.of(
                    "~rfc 2616 section 3.1.1.1",
                    Sofia.rfcSucceed(
                        "https://tools.ietf.org/html/rfc2616#section-3.1.1.1",
                        "Hypertext Transfer Protocol -- HTTP/1.1",
                    ),
                ),
                Arguments.of(
                    "~rfc 2616 Section 3.1.1.1",
                    Sofia.rfcSucceed(
                        "https://tools.ietf.org/html/rfc2616#section-3.1.1.1",
                        "Hypertext Transfer Protocol -- HTTP/1.1",
                    ),
                ),
                Arguments.of(
                    "~rfc 2616 page 11",
                    Sofia.rfcSucceed(
                        "https://tools.ietf.org/html/rfc2616#page-11",
                        "Hypertext Transfer Protocol -- HTTP/1.1",
                    ),
                ),
                Arguments.of(
                    "~rfc 2616 Page 11",
                    Sofia.rfcSucceed(
                        "https://tools.ietf.org/html/rfc2616#page-11",
                        "Hypertext Transfer Protocol -- HTTP/1.1",
                    ),
                ),

                // Only return base RFC url if specific section or page not indicated or correct
                Arguments.of(
                    "~rfc 2616 blarg 22",
                    Sofia.rfcSucceed(
                        "https://tools.ietf.org/html/rfc2616",
                        "Hypertext Transfer Protocol -- HTTP/1.1",
                    ),
                ),
                Arguments.of(
                    "~rfc 2616 section",
                    Sofia.rfcSucceed(
                        "https://tools.ietf.org/html/rfc2616",
                        "Hypertext Transfer Protocol -- HTTP/1.1",
                    ),
                ),
                Arguments.of(
                    "~rfc 2616 page",
                    Sofia.rfcSucceed(
                        "https://tools.ietf.org/html/rfc2616",
                        "Hypertext Transfer Protocol -- HTTP/1.1",
                    ),
                ),
            )
    }

    @ParameterizedTest
    @MethodSource("getRfcsCommandAndExpectedMessages")
    fun testRfcMessage(text: String, responseValue: String) {
        assertEquals(responseValue, operation.handleMessage(message(text))[0].value)
    }
}
