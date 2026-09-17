package javabot.qtest.model

import io.quarkus.test.junit.QuarkusTest
import javabot.BaseTest
import javabot.model.ChannelEvent
import javabot.model.EventType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
class ChannelEventTest : BaseTest() {
    @BeforeEach
    @Test
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
        Assertions.assertNotNull(channelDao.get(name))
    }

    @Test
    fun addKeyedChannel() {
        bot
        val key = "abcdef"
        val event = ChannelEvent("testng", EventType.ADD, KEYED_CHANNEL, key)
        eventDao.save(event)
        waitForEvent(event, "adding keyed channel " + KEYED_CHANNEL)
        Assertions.assertNotNull(channelDao.get(KEYED_CHANNEL))
    }

    @Test
    fun leave() {
        bot
        val event = ChannelEvent("testng", EventType.DELETE, KEYED_CHANNEL)
        eventDao.save(event)
        waitForEvent(event, "leaving channel " + KEYED_CHANNEL)
        Assertions.assertNull(channelDao.get(KEYED_CHANNEL))
    }

    @Test
    fun update() {
        val name = "##testChannel"
        val event = ChannelEvent("testng", EventType.UPDATE, name, "newKey")
        eventDao.save(event)
        waitForEvent(event, "updating channel " + name)
        val channel = channelDao.get(name)
        Assertions.assertNotNull(channel)
        Assertions.assertEquals("newKey", channel?.key)
    }

    companion object {
        private val KEYED_CHANNEL = "##testKeyedChannel"
    }
}
