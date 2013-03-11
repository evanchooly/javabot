package javabot.model;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import com.antwerkz.sofia.Sofia;
import com.google.inject.Injector;
import javabot.BaseTest;
import javabot.TestJavabot;
import javabot.dao.EventDao;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(enabled = false)
public class ChannelEventTest extends BaseTest {
  private CountDownLatch latch;
  @Inject
  private Injector injector;
  @Inject
  private EventDao dao;
  private String chanName;
  private String chanKey;
  private String message;

  @Override
  protected TestJavabot createBot() {
    ChannelEventTestJavabot bot = injector.getInstance(ChannelEventTestJavabot.class);
    bot.start();
    return bot;
  }

  @Test
  public void addChannel() throws InterruptedException {
    getJavabot();
    String name = "##testChannel";
    ChannelEvent event = new ChannelEvent(name, EventType.ADD, "testng");
    latch = new CountDownLatch(1);
    dao.save(event);
    latch.await(60, TimeUnit.SECONDS);
    Assert.assertEquals(latch.getCount(), 0);
    Assert.assertEquals(chanName, name);
    Assert.assertNull(chanKey);
  }

  @Test
  public void addKeyedChannel() throws InterruptedException {
    String name = "##testChannel";
    String key = "abcdef";
    ChannelEvent event = new ChannelEvent(name, key, EventType.ADD, "testng");
    latch = new CountDownLatch(1);
    dao.save(event);
    latch.await(60, TimeUnit.SECONDS);
    Assert.assertEquals(latch.getCount(), 0);
    Assert.assertEquals(chanName, name);
    Assert.assertEquals(chanKey, key);
  }

  @Test
  public void leave() throws InterruptedException {
    String name = "##testChannel";
    ChannelEvent event = new ChannelEvent(name, EventType.DELETE, "testng");
    latch = new CountDownLatch(1);
    dao.save(event);
    latch.await(60, TimeUnit.SECONDS);
    Assert.assertEquals(latch.getCount(), 0);
    Assert.assertEquals(name, name);
    Assert.assertEquals(message, Sofia.channelDeleted("testng"));
  }

  @Test
  public void update() throws InterruptedException {
    String name = "##testChannel";
    ChannelEvent event = new ChannelEvent(name, EventType.UPDATE, "testng");
    latch = new CountDownLatch(2);
    dao.save(event);
    latch.await(60, TimeUnit.SECONDS);
    Assert.assertEquals(latch.getCount(), 0);
    Assert.assertEquals(name, name);
    Assert.assertEquals(message, Sofia.channelUpdated());
  }

  private class ChannelEventTestJavabot extends TestJavabot {
    @Override
    public void join(String name, String key) {
      chanName = name;
      chanKey = key;
      latch.countDown();
    }

    @Override
    public void leave(String name, String reason) {
      chanName = name;
      message = reason;
      latch.countDown();
    }
  }
}
