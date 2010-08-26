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
import org.testng.annotations.AfterSuite;

@SuppressWarnings({"StaticNonFinalField"})
public class BaseTest {
    protected static final String TEST_BOT = "jbtestbot";
    public static final String TEST_USER = "jbtestuser";
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    @Autowired
    private AdminDao dao;
    public final String ok;
    private static TestJavabot bot;
    private static TestBot user;
    private final ApplicationContext context;

    public BaseTest() {
        context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        inject(this);
        createBot();
        final String nick = BaseTest.TEST_USER;
        ok = "OK, " + nick.substring(0, Math.min(nick.length(), 16)) + ".";
        sleep(2000);
    }

    protected final Javabot createBot() {
        if (bot == null) {
            try {
                bot = new TestJavabot(context);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return bot;
    }

    public TestJavabot getJavabot() {
        if (bot == null) {
            createBot();
        }
        return bot;
    }

    public final TestBot getTestBot() {
        synchronized (context) {
            if (user == null) {
                user = new TestBot(TEST_USER);
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

    }

    @AfterSuite
    public void shutdown() throws InterruptedException {
        Thread.sleep(3000);
        getJavabot().shutdown();
    }

    public class TestJavabot extends Javabot {
        private final List<Message> messages = new ArrayList<Message>();

        public TestJavabot(final ApplicationContext context) throws IOException {
            super(context);
        }

        public List<Message> getMessages() {
            final List<Message> list = new ArrayList<Message>(messages);
            messages.clear();
            return list;
        }

        @Override
        public void onJoin(final String channel, final String sender, final String login,
            final String hostname) {
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
                dao.create(BaseTest.TEST_USER, "localhost");
                } catch (Exception e) {
                log.debug(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        @Override
        public void connect() {
            try {
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

        @Override
        void postAction(final Message message) {
            postMessage(message);
        }

        @Override
        void postMessage(final Message message) {
            logMessage(message);
            messages.add(message);
        }

        @Override
        public boolean userIsOnChannel(final String nick, final String channel) {
            return true;
        }
    }
}