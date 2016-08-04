package javabot

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
        assertEquals(Message.extractContentFromMessage(channel, user, "~", "~ping").value, "ping")
        assertEquals(Message.extractContentFromMessage(channel, user, "~", "~flibbity++").value, "flibbity++")
    }

    @Test
    fun botName() {
        assertEquals(Message.extractContentFromMessage(channel, user, "javabot", "javabot: ping").value, "ping")
        assertEquals(Message.extractContentFromMessage(channel, user, "javabot", "javabot: flibbity++").value, "flibbity++")
    }
}