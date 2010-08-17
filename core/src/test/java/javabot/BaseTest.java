package javabot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javabot.dao.AdminDao;
import javabot.model.Channel;
import javabot.model.Config;
import org.jibble.pircbot.PircBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;

@SuppressWarnings({"StaticNonFinalField"})
public class BaseTest {
    protected static final String TEST_BOT = "jbtestbot";
    public static final String TEST_USER = "jbtestuser";
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    @Autowired
    private AdminDao dao;
    public final String ok;
    private static Javabot bot;
    private static TestBot user;
    private final ApplicationContext context;

    public BaseTest() {
        context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        inject(this);
        createBot();
        final String nick = getTestBot().getNick();
        ok = "OK, " + nick.substring(0, Math.min(nick.length(), 16)) + ".";
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

                @Override
                public String getNick() {
                    return TEST_BOT;
                }

                public void loadConfig(final Config config) {
                    try {
                        log.debug("Running with configuration: " + config);
                        setName(getNick());
                        setLogin(getNick());
                        setNickPassword(config.getPassword());
                        setStartStrings("~");
                    } catch (Exception e) {
                        log.debug(e.getMessage(), e);
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }

                @Override
                public void connect() {
                    try {
                        connect("irc.freenode.net", 6667);
                        Channel chan = channelDao.get(getJavabotChannel());
                        if (chan == null) {
                            chan = new Channel();
                            chan.setName("##" + getNick());
                            System.out.println("No channels found.  Initializing to " + chan.getName());
                            channelDao.save(chan);
                        }
                        chan.join(this);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            };
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
            if (user == null) {
                user = new TestBot(TEST_USER);
                try {
                    user.connect("irc.freenode.net");
                    user.joinChannel(getJavabotChannel());
                } catch (Exception e) {
                    log.debug(e.getMessage(), e);
                    Assert.fail(e.getMessage());
                }
            }
            return user;
        }
    }

    protected final void inject(final Object object) {
        context.getAutowireCapableBeanFactory().autowireBean(object);
    }

    protected String getJavabotChannel() {
        return "#jbunittest";
    }

    @SuppressWarnings({"EmptyCatchBlock"})
    protected static void sleep(final int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
        }
    }

    protected void waitForResponses(final TestBot bot, final int length) {
        int count = 10;
        while (length != 0 && count != 0 && bot.getResponseCount() != length) {
            try {
                Thread.sleep(1000);
                count--;
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        Assert.assertEquals(bot.getResponseCount(), length);
    }

    public static class TestBot extends PircBot {
        private final List<Response> responses = new ArrayList<Response>();

        public TestBot(final String name) {
            final Properties props = new Properties();
            try {
                props.load(getClass().getResourceAsStream("/locations-override.properties"));
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