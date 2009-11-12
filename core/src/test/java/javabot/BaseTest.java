package javabot;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import javabot.dao.AdminDao;
import org.jibble.pircbot.PircBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"StaticNonFinalField"})
public class BaseTest {
    public static final String TARGET_TEST_BOT = "jbtargetbot";
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    @Autowired
    private AdminDao dao;
    public final String ok;
    private static Javabot bot;
    private static TestBot testBot;
    private static TestBot testTargetBot;
    private final ApplicationContext context;

    public BaseTest() {
        context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        inject(this);
        createBot();
        final String nick = getTestBot().getNick();
        ok = "OK, " + nick.substring(0, Math.min(nick.length(), 16)) + ".";
        getTestBot();
        getTestTargetBot();
        sleep(2000);
    }

    protected final Javabot createBot() {
        if (bot == null) {
            bot = new Javabot(context) {
                @Override
                public void onJoin(final String channel, final String sender, final String login,
                    final String hostname) {
                    dao.create(sender, hostname);
                    super.onJoin(channel, sender, login, hostname);
                }
            };
            bot.setStartStrings(new String[] {"~", bot.getNick()});
            inject(bot);
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
                testBot = new TestBot("jbunittestbot");
                try {
                    testBot.connect("irc.freenode.net");
                    testBot.joinChannel(getJavabotChannel());
                } catch (Exception e) {
                    log.debug(e.getMessage(), e);
                    Assert.fail(e.getMessage());
                }
            }
            return testBot;
        }
    }
    public final TestBot getTestTargetBot() {
        synchronized (context) {
            if (testTargetBot == null) {
                testTargetBot = new TestBot(TARGET_TEST_BOT);
                try {
                    testTargetBot.connect("irc.freenode.net");
                    testTargetBot.joinChannel(getJavabotChannel());
                } catch (Exception e) {
                    log.debug(e.getMessage(), e);
                    Assert.fail(e.getMessage());
                }
            }
            return testTargetBot;
        }
    }

    protected final void inject(final Object object) {
        context.getAutowireCapableBeanFactory().autowireBean(object);
    }

    protected String getJavabotChannel() {
        return getJavabot().getChannels()[0];
    }

    @SuppressWarnings({"EmptyCatchBlock"})
    protected static void sleep(final int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
        }
    }

    public static class TestBot extends PircBot {
        private final List<Response> responses = new ArrayList<Response>();

        public TestBot(final String name) {
            final InputStream asStream = getClass().getResourceAsStream("/locations-override.properties");
            final Properties props = new Properties();
            try {
                props.load(asStream);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage());
            }
            setName(name);
        }

        @Override
        protected void onAction(final String sender, final String login, final String hostname, final String target,
            final String action) {
            responses.add(new Response(target, sender, login, hostname, action));

        }

        @Override
        public void onMessage(final String channel, final String sender, final String login,
            final String hostname, final String message) {
            responses.add(new Response(channel, sender, login, hostname, message));
        }

        @Override
        protected void onPrivateMessage(final String sender, final String login, final String hostname,
            final String message) {
            log.debug(new Response(sender, sender, login, hostname, message).toString());
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