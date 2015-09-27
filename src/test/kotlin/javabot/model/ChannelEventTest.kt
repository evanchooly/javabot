package javabot.model

import com.google.inject.Injector
import com.jayway.awaitility.Duration
import javabot.BaseTest
import org.testng.Assert
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import javax.inject.Inject

@Test
public class ChannelEventTest : BaseTest() {
    @Inject
    lateinit val injector: Injector

    @BeforeTest
    public fun clearEvents() {
        for (event in eventDao.findAll()) {
            eventDao.delete(event)
        }
    }

    @Test
    @Throws(InterruptedException::class)
    public fun addChannel() {
        javabot
        channelDao.delete(channelDao.get("##testChannel"))
        val name = "##testChannel"
        val event = ChannelEvent(name, EventType.ADD, "testng")
        eventDao.save(event)
        waitForEvent(event, "adding channel " + name, Duration.ONE_MINUTE)
        Assert.assertNotNull(channelDao.get(name))
    }

    @Test
    @Throws(InterruptedException::class)
    public fun addKeyedChannel() {
        javabot
        val key = "abcdef"
        val event = ChannelEvent(KEYED_CHANNEL, key, EventType.ADD, "testng")
        eventDao.save(event)
        waitForEvent(event, "adding keyed channel " + KEYED_CHANNEL, Duration.ONE_MINUTE)
        Assert.assertNotNull(channelDao.get(KEYED_CHANNEL))
    }

    @Test
    @Throws(InterruptedException::class)
    public fun leave() {
        javabot
        val event = ChannelEvent(KEYED_CHANNEL, EventType.DELETE, "testng")
        eventDao.save(event)
        waitForEvent(event, "leaving channel " + KEYED_CHANNEL, Duration.ONE_MINUTE)
        Assert.assertNull(channelDao.get(KEYED_CHANNEL))
    }

    @Test
    @Throws(InterruptedException::class)
    public fun update() {
        val name = "##testChannel"
        val event = ChannelEvent(name, "newKey", EventType.UPDATE, "testng")
        eventDao.save(event)
        waitForEvent(event, "updating channel " + name, Duration.ONE_MINUTE)
        val channel = channelDao.get(name)
        Assert.assertNotNull(channel)
        Assert.assertEquals(channel?.key, "newKey")

    }

    companion object {
        private val KEYED_CHANNEL = "##testKeyedChannel"
    }
}