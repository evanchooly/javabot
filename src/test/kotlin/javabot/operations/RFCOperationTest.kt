package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseMessagingTest
import org.testng.annotations.Test

import javax.inject.Inject

import org.testng.Assert.assertEquals

@Test(groups = arrayOf("operations"))
public class RFCOperationTest : BaseMessagingTest() {
    @Inject
    lateinit var operation: RFCOperation

    @Test
    public fun testBadRFCNumber() {
        testMessage("~rfc abd", Sofia.rfcInvalid("abd"))
    }

    @Test
    public fun testMissingRFCNumber() {
        // these should be handled by the factoid operation
        testMessage("~rfc ", Sofia.unhandledMessage(testUser))
        testMessage("~rfc", Sofia.unhandledMessage(testUser))
    }

    @Test
    public fun testHTTPRFC() {
        val hitRate = operation.rfcTitleCache.stats().hitCount()
        testMessage("~rfc 2616",
              Sofia.rfcSucceed("http://www.faqs.org/rfcs/rfc2616.html",
                    "RFC 2616 - Hypertext Transfer Protocol -- HTTP/1.1 (RFC2616)"))
        testMessage("~rfc 2616",
              Sofia.rfcSucceed("http://www.faqs.org/rfcs/rfc2616.html",
                    "RFC 2616 - Hypertext Transfer Protocol -- HTTP/1.1 (RFC2616)"))
        assertEquals(operation.rfcTitleCache.stats().hitCount(), hitRate + 1)
    }

    public fun testNonexistentRFC() {
        // sofia: rfc.fail
        testMessage("~rfc 2616132", Sofia.rfcFail("2616132"))
    }

}
