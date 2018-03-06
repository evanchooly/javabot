package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import javax.inject.Inject

@Test(groups = arrayOf("operations")) class RFCOperationTest : BaseTest() {
    @Inject
    private lateinit var operation: RFCOperation

    @DataProvider(name = "rfcMessages")
    fun getRfcsCommandAndExpectedMessages(): Array<Array<*>> {
        return arrayOf(
                arrayOf("~rfc abd", Sofia.rfcInvalid("abd")),
                arrayOf("~rfc", Sofia.rfcMissing()),
                arrayOf("~rfc 2616", Sofia.rfcSucceed("https://tools.ietf.org/html/rfc2616", "RFC 2616 - Hypertext Transfer Protocol -- HTTP/1.1")),
                arrayOf("~rfc 8675309", Sofia.rfcFail("8675309"))
        )
    }

    @Test(dataProvider = "rfcMessages")
    fun testRfcMessage(text: String, responseValue: String)  {
        Assert.assertEquals(operation.handleMessage(message(text))[0].value, responseValue)
    }
}
