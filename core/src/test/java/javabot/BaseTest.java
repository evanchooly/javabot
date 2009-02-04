package javabot;

import java.util.ArrayList;
import java.util.List;

import javabot.dao.AdminDao;
import org.jibble.pircbot.PircBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class BaseTest {
    @Autowired
    private AdminDao dao;

    public final String ok;
    private static Javabot bot;
    private static TestBot testBot;
    private final ApplicationContext context;

    public BaseTest() {
        context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        createBot();
        ok = "Okay, " + getTestBot().getNick().substring(0, 16) + ".";
        inject(this);
    }

    protected final Javabot createBot() {
        if (bot == null) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    bot = new Javabot(context) {
                        @Override
                        public void onJoin(final String channel, final String sender, final String login,
                            final String hostname) {
                            dao.create(sender, hostname);
                            super.onJoin(channel, sender, login, hostname);
                        }
                    };
//                }
//            }).start();
//        }
//        while(bot == null) {
//            Thread.yield();
        }
        return bot;
    }

    public Javabot getJavabot() {
        if (bot == null) {
            createBot();
        }
        return bot;
    }

    public final TestBot getTestBot() {
        synchronized (context) {
            if (testBot == null) {
                testBot = new TestBot();
                try {
                    testBot.connect("irc.freenode.net");
                    testBot.joinChannel(getJavabotChannel());
                } catch (Exception e) {
                    Assert.fail(e.getMessage());
                }
            }
            return testBot;
        }
    }

    protected final void inject(final Object object) {
        context.getAutowireCapableBeanFactory().autowireBean(object);
    }

    protected String getJavabotChannel() {
        return getJavabot().getChannels()[0];
    }

    public static class TestBot extends PircBot {
        private final List<Response> responses = new ArrayList<Response>();

        public TestBot() {
            final String s = "javabot" + System.currentTimeMillis();
            setName(s.substring(0,16));
        }

        @Override
        public void onMessage(final String channel, final String sender, final String login,
            final String hostname, final String message) {
            responses.add(new Response(channel, sender, login, hostname, message));
        }

        @Override
        protected void onPrivateMessage(final String sender, final String login, final String hostname,
            final String message) {
            onMessage(sender, sender, login, hostname, message);
        }

        public int getResponseCount() {
            return responses.size();
        }
        
        public Response getOldestResponse() {
            return responses.isEmpty() ? null : responses.remove(0);
        }

        public String getOldestMessage() {
            final Response response = getOldestResponse();
            return response == null ? null : response.getMessage();
        }
    }

    @AfterSuite
    public void shutdown() throws InterruptedException {
        Thread.sleep(3000);
        getJavabot().shutdown();
    }
}