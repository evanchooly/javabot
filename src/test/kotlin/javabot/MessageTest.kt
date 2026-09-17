package javabot

import io.quarkus.test.junit.QuarkusTest
import javabot.model.Channel
import javabot.model.JavabotUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@QuarkusTest
class MessageTest : BaseTest() {
    companion object {
        val channel = Channel("#test")
        val user = JavabotUser("somejoe")
    }

    @Test
    fun triggerCharacter() {
        check("~", "~ping", "ping")
        check("~", "~flibbity++", "flibbity++")
        check("~", "flibbity++", "flibbity++", false)
    }

    @Test
    fun botName() {
        check("javabot", "javabot: ping", "ping")
        check("javabot", "javabot: flibbity++", "flibbity++")
        check("javabot", "flibbity++", "flibbity++", false)
    }

    private fun check(start: String, test: String, expected: String, triggered: Boolean = true) {
        val message =
            Message.extractContentFromMessage(
                bot.get(),
                channel,
                user,
                start,
                BaseTest.TEST_BOT_NICK,
                test,
            )
        assertEquals(expected, message.value)
        assertEquals(triggered, message.triggered)
    }
}
