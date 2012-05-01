package javabot.model;

import com.antwerkz.sofia.Sofia;
import javabot.BaseTest;
import javabot.dao.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ChannelEventTest extends BaseTest {

    private CountDownLatch latch;
    @Autowired
    private EventDao dao;
    private String chanName;
    private String chanKey;
    private String message;

    @Override
    protected TestJavabot createBot() {
        return new TestJavabot(getContext()) {
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
        };
    }

    @Test
    public void addChannel() throws InterruptedException {
        TestJavabot javabot = getJavabot();
        String name = "##testChannel";
        ChannelEvent event = new ChannelEvent(name, ChannelEventType.ADD, "testng");
        latch = new CountDownLatch(1);
        dao.save(event);
        latch.await(60, TimeUnit.SECONDS);
        Assert.assertEquals(latch.getCount(), 0);
        Assert.assertEquals(chanName, name);
        Assert.assertNull(chanKey);
    }

    @Test
    public void addKeyedChannel() throws InterruptedException {
        TestJavabot javabot = getJavabot();
        String name = "##testChannel";
        String key = "abcdef";
        ChannelEvent event = new ChannelEvent(name, key, ChannelEventType.ADD, "testng");
        latch = new CountDownLatch(1);
        dao.save(event);
        latch.await(60, TimeUnit.SECONDS);
        Assert.assertEquals(latch.getCount(), 0);
        Assert.assertEquals(chanName, name);
        Assert.assertEquals(chanKey, key);
    }

    @Test
    public void leave() throws InterruptedException {
        TestJavabot javabot = getJavabot();
        String name = "##testChannel";
        ChannelEvent event = new ChannelEvent(name, ChannelEventType.DELETE, "testng");
        latch = new CountDownLatch(1);
        dao.save(event);
        latch.await(60, TimeUnit.SECONDS);
        Assert.assertEquals(latch.getCount(), 0);
        Assert.assertEquals(name, name);
        Assert.assertEquals(message, Sofia.channelDeleted("testng"));
    }

    @Test
    public void update() throws InterruptedException {
        TestJavabot javabot = getJavabot();
        String name = "##testChannel";
        ChannelEvent event = new ChannelEvent(name, ChannelEventType.UPDATE, "testng");
        latch = new CountDownLatch(2);
        dao.save(event);
        latch.await(60, TimeUnit.SECONDS);
        Assert.assertEquals(latch.getCount(), 0);
        Assert.assertEquals(name, name);
        Assert.assertEquals(message, Sofia.channelUpdated());
    }
}
