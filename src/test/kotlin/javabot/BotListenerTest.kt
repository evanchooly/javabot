package javabot

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import com.jayway.awaitility.Duration
import javabot.dao.FactoidDao
import javabot.dao.LogsDaoTest
import javabot.dao.LogsDaoTest.Companion
import org.pircbotx.hooks.events.MessageEvent
import org.pircbotx.hooks.events.PrivateMessageEvent
import org.testng.Assert
import org.testng.annotations.Test
import java.util.concurrent.TimeUnit

class BotListenerTest: BaseTest() {
    @Inject
    lateinit var listener: BotListener
    @Inject
    lateinit var factoidDao: FactoidDao

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

    @Test
    fun factoidLookup() {
        factoidDao.delete(testUser.nick, "impact", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(testUser.nick, "impact", "<reply>ouch", LogsDaoTest.CHANNEL_NAME)
        listener.onMessage(MessageEvent(ircBot.get(), testChannel, testUser, "~impact"))
        Assert.assertEquals(messages.get(Duration(10, TimeUnit.MINUTES))[0], "ouch")

    }

    @Test
    fun tell() {
        factoidDao.delete(testUser.nick, "impact", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(testUser.nick, "impact", "<reply>ouch", LogsDaoTest.CHANNEL_NAME)
        listener.onMessage(MessageEvent(ircBot.get(), testChannel, testUser, "~~ ${targetUser.nick} impact"))
        Assert.assertEquals(messages.get(Duration(10, TimeUnit.MINUTES))[0], "${targetUser.nick}, ouch")
    }
}