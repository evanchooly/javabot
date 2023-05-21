package javabot.model

import javabot.BaseTest
import org.testng.Assert
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

@Test
class ChannelEventTest : BaseTest() {
    @BeforeTest
    fun clearEvents() {
        for (event in eventDao.findAll()) {
            eventDao.delete(event)
        }
    }

    @Test
    fun addChannel() {
        bot
        channelDao.delete(channelDao.get("##testChannel"))
        val name = "##testChannel"
        val event = ChannelEvent("testng", EventType.ADD, name)
        eventDao.save(event)
        waitForEvent(event, "adding channel $name.  event id: ${event.id}")
        Assert.assertNotNull(channelDao.get(name))
    }

    @Test
    fun addKeyedChannel() {
        bot
        val key = "abcdef"
        val event = ChannelEvent("testng", EventType.ADD, KEYED_CHANNEL, key)
        eventDao.save(event)
        waitForEvent(event, "adding keyed channel " + KEYED_CHANNEL)
        Assert.assertNotNull(channelDao.get(KEYED_CHANNEL))
    }

    @Test
    fun leave() {
        bot
        val event = ChannelEvent("testng", EventType.DELETE, KEYED_CHANNEL)
        eventDao.save(event)
        waitForEvent(event, "leaving channel " + KEYED_CHANNEL)
        Assert.assertNull(channelDao.get(KEYED_CHANNEL))
    }

    @Test
    fun update() {
        val name = "##testChannel"
        val event = ChannelEvent("testng", EventType.UPDATE, name, "newKey")
        eventDao.save(event)
        waitForEvent(event, "updating channel " + name)
        val channel = channelDao.get(name)
        Assert.assertNotNull(channel)
        Assert.assertEquals(channel?.key, "newKey")
    }

    companion object {
        private val KEYED_CHANNEL = "##testKeyedChannel"
    }
}
