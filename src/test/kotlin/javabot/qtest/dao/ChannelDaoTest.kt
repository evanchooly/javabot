package javabot.qtest.dao

import dev.morphia.DeleteOptions
import io.quarkus.test.junit.QuarkusTest
import javabot.dao.BaseServiceTest
import javabot.model.Channel
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@QuarkusTest
class ChannelDaoTest : BaseServiceTest() {
    @Test
    fun addChannel() {
        val channel = Channel()
        val name = "##" + System.currentTimeMillis()
        channel.name = name
        channel.logged = true
        channelDao.save(channel)
        assertNotNull(channelDao.get(name))
    }

    @Test
    fun getChannels() {
        assertTrue(!channelDao.getChannels().isEmpty())
    }

    @Test
    fun currentChannels() {
        assertTrue(!channelDao.configuredChannels().isEmpty())
    }

    @Test
    @Disabled
    fun stats() {
        datastore.find(Channel::class.java).delete(DeleteOptions().multi(true))
        val list = channelDao.getStatistics()
        assertTrue(list.isNotEmpty())
        val activity = list[0]
        assertNotSame(0, activity.total)
        assertNotNull(activity.getPercent())
    }
}
