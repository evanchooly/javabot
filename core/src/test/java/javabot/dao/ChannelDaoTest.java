package javabot.dao;

import java.util.List;
import javax.inject.Inject;

import javabot.model.Activity;
import javabot.model.Channel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ChannelDaoTest extends BaseServiceTest {
  @Inject
  private ChannelDao channelDao;

  @Test
  public void addChannel() {
    final Channel channel = new Channel();
    final String name = "##" + System.currentTimeMillis();
    channel.setName(name);
    channel.setLogged(true);
    channelDao.save(channel);
    Assert.assertNotNull(channelDao.get(name));
  }

  @Test
  public void getChannels() {
    final List channels = channelDao.getChannels();
    Assert.assertTrue(!channels.isEmpty());

  }

  @Test
  public void currentChannels() {
    final List channels = channelDao.configuredChannels();
    Assert.assertTrue(!channels.isEmpty());
  }

  @Test(enabled=false)
  public void stats() {
    final List<Activity> list = channelDao.getStatistics();
    Assert.assertTrue(!list.isEmpty());
    final Activity activity = list.get(0);
    Assert.assertNotSame(activity.getTotal(), 0);
    Assert.assertNotNull(activity.getPercent());
  }
}