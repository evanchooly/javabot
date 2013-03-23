package javabot.model;

import javax.inject.Inject;

import com.google.inject.Injector;
import com.jayway.awaitility.Duration;
import javabot.BaseTest;
import javabot.dao.ChannelDao;
import javabot.dao.EventDao;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
public class ChannelEventTest extends BaseTest {
  @Inject
  private Injector injector;
  @Inject
  private EventDao dao;
  @Inject
  private ChannelDao channelDao;

  private static final String KEYED_CHANNEL = "##testKeyedChannel";

  @BeforeTest
  public void clearEvents() {
    for (AdminEvent event : dao.findAll()) {
      dao.delete(event);
    }
  }
  @Test
  public void addChannel() throws InterruptedException {
    getJavabot();
    String name = "##testChannel";
    ChannelEvent event = new ChannelEvent(name, EventType.ADD, "testng");
    dao.save(event);
    waitForEvent(event, "adding channel " + name, Duration.ONE_MINUTE);
    Assert.assertNotNull(channelDao.get(name));
  }

  @Test
  public void addKeyedChannel() throws InterruptedException {
    getJavabot();
    String key = "abcdef";
    ChannelEvent event = new ChannelEvent(KEYED_CHANNEL, key, EventType.ADD, "testng");
    dao.save(event);
    waitForEvent(event, "adding keyed channel " + KEYED_CHANNEL, Duration.ONE_MINUTE);
    Assert.assertNotNull(channelDao.get(KEYED_CHANNEL));
  }

  @Test
  public void leave() throws InterruptedException {
    getJavabot();
    ChannelEvent event = new ChannelEvent(KEYED_CHANNEL, EventType.DELETE, "testng");
    dao.save(event);
    waitForEvent(event, "leaving channel " + KEYED_CHANNEL, Duration.ONE_MINUTE);
    Assert.assertNull(channelDao.get(KEYED_CHANNEL));
  }

  @Test
  public void update() throws InterruptedException {
    String name = "##testChannel";
    ChannelEvent event = new ChannelEvent(name, "newKey", EventType.UPDATE, "testng");
    dao.save(event);
    waitForEvent(event, "updating channel " + name, Duration.ONE_MINUTE);
    Channel channel = channelDao.get(name);
    Assert.assertNotNull(channel);
    Assert.assertEquals(channel.getKey(), "newKey");

  }
}