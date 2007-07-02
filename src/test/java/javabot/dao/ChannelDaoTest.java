package javabot.dao;

import java.util.List;

import javabot.model.Channel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

public class ChannelDaoTest extends BaseServiceTest {
    @SpringBeanByType
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