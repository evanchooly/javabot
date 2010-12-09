package javabot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javabot.dao.AdminDao;
import org.schwering.irc.lib.IRCUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterSuite;

@SuppressWarnings({"StaticNonFinalField"})
public class BaseTest {
    protected static final String TEST_BOT = "jbtestbot";
    public static final IRCUser TEST_USER = new IRCUser("jbtestuser", "jbtestuser", "localhost");
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
        final String nick = BaseTest.TEST_USER.getNick();
        ok = "OK, " + nick.substring(0, Math.min(nick.length(), 16)) + ".";
        sleep(2000);
    }

    protected final Javabot createBot() {
        if (bot == null) {
            bot = new TestJavabot(context);
        }
        return bot;
    }

    public final TestJavabot getJavabot() {
        if (bot == null) {
            createBot();
        }
        return bot;
    }

    public final TestBot getTestBot() {
        synchronized (context) {
            if (user == null) {
                user = new TestBot();
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

    public class TestBot {
        private final List<Response> responses = new ArrayList<Response>();

        public TestBot() {
            final Properties props = new Properties();
            try {
                final InputStream resourceAsStream = getClass().getResourceAsStream("/spring-context-override.properties");
                try {
                    if (resourceAsStream != null) {
                        props.load(resourceAsStream);
                    }
                } finally {
                    if (resourceAsStream != null) {
                        resourceAsStream.close();
                    }
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage());
            }
        }

        protected void onAction(final String sender, final String login, final String hostname, final String target,
                                final String action) {
//            responses.add(new Response(target, sender, login, hostname, action));

        }

//        @Override
        public void onPrivmsg(final String target, final IRCUser user, final String msg) {
            responses.add(new Response(target, user, msg));
        }

//        @Override
//        protected void onPrivateMessage(final String sender, final String login, final String hostname,
//                                        final String message) {
//            log.debug(new Response(sender, sender, login, hostname, message).toString());
//            onMessage(sender, sender, login, hostname, message);
//        }

    }

    @AfterSuite
    public void shutdown() throws InterruptedException {
//        getJavabot().shutdown();
    }

    public class TestJavabot extends Javabot {
        private final List<Message> messages = new ArrayList<Message>();

        public TestJavabot(final ApplicationContext context) {
            super(context);
        }

        public List<Message> getMessages() {
            final List<Message> list = new ArrayList<Message>(messages);
            messages.clear();
            return list;
        }

        @Override
        public String getNick() {
            return TEST_BOT;
        }

        @Override
        public void loadConfig() {
            try {
                log.debug("Running with configuration: " + config);
                setStartStrings("~");
                if(dao.getAdmin(BaseTest.TEST_USER.getNick(), "localhost") == null) {
                    dao.create(BaseTest.TEST_USER.getNick(), "localhost");
                }
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        @Override
        public void connect() {
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
        public boolean userIsOnChannel(final IRCUser sender, final String channel) {
            return true;
        }
    }
}