package javabot

import javabot.BaseTest.Companion
import javabot.model.Channel
import javabot.model.JavabotUser
import org.testng.Assert.*
import org.testng.annotations.Test

class MessageTest {
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
        val message = Message.extractContentFromMessage(channel, user, start, BaseTest.TEST_BOT_NICK, test)
        assertEquals(message.value, expected)
        assertEquals(message.triggered, triggered)
    }
}