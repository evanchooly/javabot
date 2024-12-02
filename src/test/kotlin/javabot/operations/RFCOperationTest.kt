package javabot.operations

import com.antwerkz.sofia.Sofia
import jakarta.inject.Inject
import javabot.BaseTest
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test(groups = arrayOf("operations"))
class RFCOperationTest : BaseTest() {
    @Inject private lateinit var operation: RFCOperation

    @DataProvider(name = "rfcMessages")
    fun getRfcsCommandAndExpectedMessages(): Array<Array<*>> {
        return arrayOf(
            arrayOf("~rfc abd", Sofia.rfcInvalid("abd")),
            arrayOf("~rfc", Sofia.rfcMissing()),
            arrayOf(
                "~rfc 2616",
                Sofia.rfcSucceed(
                    "https://tools.ietf.org/html/rfc2616",
                    "Hypertext Transfer Protocol -- HTTP/1.1"
                )
            ),
            arrayOf("~rfc 8675309", Sofia.rfcFail("8675309")),
            arrayOf(
                "~rfc 2616 section 3.1.1.1",
                Sofia.rfcSucceed(
                    "https://tools.ietf.org/html/rfc2616#section-3.1.1.1",
                    "Hypertext Transfer Protocol -- HTTP/1.1"
                )
            ),
            arrayOf(
                "~rfc 2616 Section 3.1.1.1",
                Sofia.rfcSucceed(
                    "https://tools.ietf.org/html/rfc2616#section-3.1.1.1",
                    "Hypertext Transfer Protocol -- HTTP/1.1"
                )
            ),
            arrayOf(
                "~rfc 2616 page 11",
                Sofia.rfcSucceed(
                    "https://tools.ietf.org/html/rfc2616#page-11",
                    "Hypertext Transfer Protocol -- HTTP/1.1"
                )
            ),
            arrayOf(
                "~rfc 2616 Page 11",
                Sofia.rfcSucceed(
                    "https://tools.ietf.org/html/rfc2616#page-11",
                    "Hypertext Transfer Protocol -- HTTP/1.1"
                )
            ),

            // Only return base RFC url if specific section or page not indicated or correct
            arrayOf(
                "~rfc 2616 blarg 22",
                Sofia.rfcSucceed(
                    "https://tools.ietf.org/html/rfc2616",
                    "Hypertext Transfer Protocol -- HTTP/1.1"
                )
            ),
            arrayOf(
                "~rfc 2616 section",
                Sofia.rfcSucceed(
                    "https://tools.ietf.org/html/rfc2616",
                    "Hypertext Transfer Protocol -- HTTP/1.1"
                )
            ),
            arrayOf(
                "~rfc 2616 page",
                Sofia.rfcSucceed(
                    "https://tools.ietf.org/html/rfc2616",
                    "Hypertext Transfer Protocol -- HTTP/1.1"
                )
            )
        )
    }

    @Test(dataProvider = "rfcMessages")
    fun testRfcMessage(text: String, responseValue: String) {
        Assert.assertEquals(operation.handleMessage(message(text))[0].value, responseValue)
    }
}
