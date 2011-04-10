package javabot;

import java.util.ArrayList;
import java.util.List;

import javabot.dao.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterSuite;

@SuppressWarnings({"StaticNonFinalField"})
public class BaseTest {
    protected static final String TEST_BOT = "jbtestbot";
    public static final IrcUser TEST_USER = new IrcUser("jbtestuser", "jbtestuser", "localhost");
    @Autowired
    private AdminDao dao;
    public final String ok;
    private static TestJavabot bot;
    private final ApplicationContext context;

    public BaseTest() {
        context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        inject(this);
        final String nick = BaseTest.TEST_USER.getNick();
        ok = "OK, " + nick.substring(0, Math.min(nick.length(), 16)) + ".";
        sleep(2000);
    }

    protected final TestJavabot createBot() {
        if (bot == null) {
            Javabot.validateProperties();
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

    @AfterSuite
    public void shutdown() throws InterruptedException {
        getJavabot().shutdown();
    }

    public class TestJavabot extends Javabot {
        private final List<Message> messages = new ArrayList<Message>();

        public TestJavabot(final ApplicationContext applicationContext) {
            super(applicationContext);
        }

        @Override
        protected void createIrcBot() {
            pircBot = new MyPircBot(this) {
                @Override
                public String getNick() {
                    return BaseTest.TEST_BOT;
                }
            };
        }

        public List<Message> getMessages() {
            final List<Message> list = new ArrayList<Message>(messages);
            messages.clear();
            return list;
        }

        @Override
        public void loadConfig() {
            try {
                super.loadConfig();
                if (dao.getAdmin(BaseTest.TEST_USER.getNick(), "localhost") == null) {
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
        public void postAction(final Message message) {
            postMessage(message);
        }

        @Override
        public void postMessage(final Message message) {
            logMessage(message);
            messages.add(message);
        }

        @Override
        public boolean userIsOnChannel(final String sender, final String channel) {
            return true;
        }
    }
}