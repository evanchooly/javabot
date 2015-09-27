package javabot.dao

import javabot.model.Channel
import org.testng.Assert
import org.testng.annotations.Test

public class ChannelDaoTest : BaseServiceTest() {
    @Test
    public fun addChannel() {
        val channel = Channel()
        val name = "##" + System.currentTimeMillis()
        channel.name = name
        channel.logged = true
        channelDao.save(channel)
        Assert.assertNotNull(channelDao.get(name))
    }

    @Test
    public fun getChannels() {
        val channels = channelDao.getChannels()
        Assert.assertTrue(!channels.isEmpty())

    }

    @Test
    public fun currentChannels() {
        val channels = channelDao.configuredChannels()
        Assert.assertTrue(!channels.isEmpty())
    }

    @Test(enabled = false)
    public fun stats() {
        val list = channelDao.getStatistics()
        Assert.assertTrue(!list.isEmpty())
        val activity = list.get(0)
        Assert.assertNotSame(activity.total, 0)
        Assert.assertNotNull(activity.getPercent())
    }
}