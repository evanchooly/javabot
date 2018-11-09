package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.MockIrcAdapter
import org.testng.Assert.*
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import javax.inject.Inject

class LinksOperationTest @Inject constructor(val operation: LinksOperation
) : BaseTest() {

    @Test
    fun testSubmitLink() {
        val response = operation.handleMessage(message("~submit http://foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name))
    }

    @Test
    fun testSubmitNoLink() {
        val response = operation.handleMessage(message("~submit foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksRejectedNoUrl())
    }

    @Test
    fun testSubmitPrivateMessageLink() {
        var response = operation.handleMessage(privateMessage("submit http://foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksNoChannel())

        response = operation.handleMessage(privateMessage("submit ${TEST_CHANNEL.name} http://foo.com This is a test"))
        assertEquals(response[0].value, Sofia.linksAccepted("http://foo.com", TEST_CHANNEL.name))
    }

}