package javabot

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import com.jayway.awaitility.Duration
import org.pircbotx.hooks.events.MessageEvent
import org.pircbotx.hooks.events.PrivateMessageEvent
import org.testng.Assert
import org.testng.annotations.Test
import java.util.concurrent.TimeUnit

class BotListenerTest: BaseTest() {
    @Inject
    lateinit var listener: BotListener

    @Test
    fun testOnMessage() {
        listener.onMessage(MessageEvent(ircBot.get(), testChannel, testUser, "~dude"))
        Assert.assertEquals(messages.get(Duration(10, TimeUnit.MINUTES))[0], Sofia.unhandledMessage(testUser.nick))
    }

    @Test
    fun testOnPrivateMessage() {
        listener.onPrivateMessage(PrivateMessageEvent(ircBot.get(), testUser, "dude"))
        Assert.assertEquals(messages.get()[0], Sofia.unhandledMessage(testUser.nick))
    }
}