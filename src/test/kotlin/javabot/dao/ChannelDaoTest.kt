package javabot.dao

import com.mongodb.BasicDBObject
import javabot.model.Channel
import org.testng.Assert
import org.testng.annotations.Test

@Test
class ChannelDaoTest : BaseServiceTest() {
    fun addChannel() {
        val channel = Channel()
        val name = "##" + System.currentTimeMillis()
        channel.name = name
        channel.logged = true
        channelDao.save(channel)
        Assert.assertNotNull(channelDao.get(name))
    }

    fun getChannels() {
        Assert.assertTrue(!channelDao.getChannels().isEmpty())

    }

    fun currentChannels() {
        Assert.assertTrue(!channelDao.configuredChannels().isEmpty())
    }

    @Test(enabled = false)
    fun stats() {
        datastore.find(Channel::class.java)
                .remove()
        val list = channelDao.getStatistics()
        Assert.assertTrue(list.isNotEmpty())
        val activity = list[0]
        Assert.assertNotSame(activity.total, 0)
        Assert.assertNotNull(activity.getPercent())
    }
}