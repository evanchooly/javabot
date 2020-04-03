package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import javabot.BaseTest
import javabot.Javabot
import javabot.MockIrcAdapter
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNotNull
import org.testng.annotations.Test

class IgnorePossessiveAddressTest
@Inject constructor(val ircAdapter: MockIrcAdapter, val javabot: Javabot) : BaseTest() {
    @Test
    fun testConfig() {
        assertNotNull(ircAdapter)
    }

    @Test
    fun testNonPossessiveAddress() {
        javabot.processMessage(message("~${javabot.nick} test"))
        assertEquals(ircAdapter.messages.messages.size, 1)
        assertEquals(ircAdapter.messages.messages[0], Sofia.unhandledMessage(TEST_USER))
        ircAdapter.messages.clear()

    }

    @Test(enabled = false)
    fun testPossessiveAddress() {
        // this message should be ignored
        javabot.processMessage(message("${javabot.nick}'s test"))
        assertEquals(ircAdapter.messages.messages.size, 0, ircAdapter.messages.messages.toString())
    }
}
