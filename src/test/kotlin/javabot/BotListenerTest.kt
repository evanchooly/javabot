package javabot

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import com.google.inject.Provider
import org.pircbotx.PircBotX
import org.pircbotx.hooks.events.PrivateMessageEvent
import org.testng.Assert
import org.testng.annotations.Test

import org.testng.Assert.*

class BotListenerTest: BaseTest() {
    @Inject
    lateinit var listener: BotListener

    @Test
    fun testOnMessage() {
        val event = PrivateMessageEvent(ircBot.get(), testUser, "dude")
        listener.onPrivateMessage(event)
        Assert.assertEquals(messages.get()[0], Sofia.unhandledMessage(testUser.nick))
    }

    @Test
    fun testOnPrivateMessage() {
        val event = PrivateMessageEvent(ircBot.get(), testUser, "dude")
        listener.onPrivateMessage(event)
        Assert.assertEquals(messages.get()[0], Sofia.unhandledMessage(testUser.nick))
    }
}