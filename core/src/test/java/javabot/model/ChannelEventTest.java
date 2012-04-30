package javabot.model;

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
            protected void processAdminEvents() {
                super.processAdminEvents();
                latch.countDown();
            }

            @Override
            public void join(String name, String key) {
                chanName = name;
                chanKey = key;
            }

            @Override
            public void leave(String name, String reason) {
                chanName = name;
                message = reason;
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
        Assert.assertEquals(message, "I was asked to leave this channel by testng");
    }
}
