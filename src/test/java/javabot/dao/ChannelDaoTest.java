package javabot.dao;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;
import javabot.dao.model.Channel;

import java.util.List;

//

// Author: joed

// Date  : Apr 15, 2007
public class ChannelDaoTest extends BaseServiceTest {

    @SpringBeanByType
    private ChannelDao channelDao;

    @Test
    public void addChannel() {

        String testing = System.currentTimeMillis() + "test";

        Channel channel = new Channel();
        channel.setChannel(testing);
        channel.setLogged(true);
        channelDao.saveOrUpdate(channel);

        Assert.assertNotNull(channelDao.getChannel(testing));


    }

    @Test
    public void getChannels() {

          List channels = channelDao.getChannels();
          Assert.assertTrue(channels.size() > 0);

    }


      @Test
    public void currentChannels() {

          List channels = channelDao.configuredChannels();
          Assert.assertTrue(channels.size() > 0);

    }

}