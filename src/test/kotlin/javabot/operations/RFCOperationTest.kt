package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import org.testng.Assert
import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import javax.inject.Inject

@Test(groups = arrayOf("operations")) class RFCOperationTest : BaseTest() {
    @Inject
    protected lateinit var operation: RFCOperation

    @Test fun testBadRFCNumber() {
        var response = operation.handleMessage(message("~rfc abd"))
        Assert.assertEquals(response[0].value, Sofia.rfcInvalid("abd"))
    }

    @Test fun testMissingRFCNumber() {
        Assert.assertEquals(operation.handleMessage(message("~rfc "))[0].value, Sofia.rfcMissing())
        Assert.assertEquals(operation.handleMessage(message("~rfc"))[0].value, Sofia.rfcMissing())
    }

    @Test fun testHTTPRFC() {
        val hitRate = operation.rfcTitleCache.stats().hitCount()
        var response = operation.handleMessage(message("~rfc 2616"))
        Assert.assertEquals(response[0].value,
                Sofia.rfcSucceed("http://www.rfc-base.org/rfc-2616.html", "RFC 2616 - Hypertext Transfer Protocol HTTP/1.1"))
        response = operation.handleMessage(message("~rfc 2616"))
        Assert.assertEquals(response[0].value,
                Sofia.rfcSucceed("http://www.rfc-base.org/rfc-2616.html", "RFC 2616 - Hypertext Transfer Protocol HTTP/1.1"))
        assertEquals(operation.rfcTitleCache.stats().hitCount(), hitRate + 1)
    }

    fun testNonexistentRFC() {
        // sofia: rfc.fail
        var response = operation.handleMessage(message("~rfc 2616132"))
        Assert.assertEquals(response[0].value, Sofia.rfcFail("2616132"))
    }

}
