package javabot.operations

import com.google.inject.Inject
import javabot.BaseTest
import javabot.MockIrcAdapter
import org.testng.Assert
import org.testng.annotations.Test

@Test(groups = arrayOf("operations"))
class SayOperationsTest : BaseTest() {
    @Inject
    private lateinit var operation: SayOperation

    fun testSay() {
        var response = operation.handleMessage(message("~say MAGNIFICENT"))
        Assert.assertEquals(response[0].value, "MAGNIFICENT")
        Assert.assertEquals(response[0].channel!!.name, TEST_CHANNEL.name)
    }

    fun testOtherChannelSay() {
        var response = operation.handleMessage(privateMessage("~say ${TEST_CHANNEL.name} hi there"))
        Assert.assertEquals(response.size, 1)
        Assert.assertEquals(response[0].value, "hi there")
        Assert.assertEquals(response[0].channel!!.name, TEST_CHANNEL.name)
    }

    @Test
    fun testSayNotOnChannel() {
        val response = operation.handleMessage(privateMessage("~say ${TEST_CHANNEL.name}a this is a test"))
        Assert.assertEquals(response.size, 0)
    }

    // TODO add a test for say that is for a channel for which user is not an op
}