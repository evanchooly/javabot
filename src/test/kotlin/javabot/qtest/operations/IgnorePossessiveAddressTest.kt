package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import javabot.BaseTest
import javabot.Javabot
import javabot.mocks.MockIrcAdapter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@QuarkusTest
class IgnorePossessiveAddressTest : BaseTest() {
    @Inject private lateinit var ircAdapter: MockIrcAdapter
    @Inject private lateinit var javabot: Javabot

    @Test
    fun testNonPossessiveAddress() {
        javabot.processMessage(message("~${javabot.nick} test"))
        assertEquals(1, ircAdapter.messages.messages.size)
        assertEquals(Sofia.unhandledMessage(TEST_USER), ircAdapter.messages.messages[0])
        ircAdapter.messages.clear()
    }

    @Test
    @Disabled
    fun testPossessiveAddress() {
        // this message should be ignored
        javabot.processMessage(message("${javabot.nick}'s test"))
        assertEquals(0, ircAdapter.messages.messages.size, ircAdapter.messages.messages.toString())
    }
}
