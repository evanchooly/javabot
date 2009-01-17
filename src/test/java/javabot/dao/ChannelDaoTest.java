package javabot.dao;

import java.util.List;

import javabot.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ChannelDaoTest extends BaseServiceTest {
    @Autowired
    private ChannelDao channelDao;

    @Test
    public void addChannel() {
        String testing = System.currentTimeMillis() + "test";
        Channel channel = new Channel();
        channel.setName(testing);
        channel.setLogged(true);
        channelDao.save(channel);
        Assert.assertNotNull(channelDao.get(testing));
    }

    @Test
    public void getChannels() {
        List channels = channelDao.getChannels();
        Assert.assertTrue(!channels.isEmpty());

    }

    @Test
    public void currentChannels() {
        List channels = channelDao.configuredChannels();
        Assert.assertTrue(!channels.isEmpty());
    }
}