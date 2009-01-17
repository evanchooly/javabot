package javabot;

import org.jibble.pircbot.PircBot;
import org.testng.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class BaseTest {
    protected static final String SENDER = "cheeser";
    protected static final String CHANNEL = "##javabot";
    protected static final String LOGIN = "";
    protected static final String HOSTNAME = "localhost";
    public static final String OKAY = "Okay, " + SENDER + ".";
    public static final String ALREADY_HAVE_FACTOID = "I already have a factoid with that name, " + SENDER;
    private static Javabot bot;
    private static PircBot testBot;
    private final ApplicationContext context;

    public BaseTest() {
        context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        createBot();
        inject(this);
    }

    protected final Javabot createBot() {
        if (bot == null) {
            bot = new Javabot(context);
            bot.joinChannel("##javabot");
        }
        return bot;
    }

    public Javabot getJavabot() {
        if (bot == null) {
            createBot();
        }
        return bot;
    }

    public PircBot getTestBot() {
        synchronized (context) {
            if (testBot == null) {
                testBot = new PircBot() {
                    {
                        setName("javabot" + System.currentTimeMillis());
                    }

                    @Override
                    protected void onMessage(final String s, final String s1, final String s2, final String s3,
                        final String s4) {
                        super.onMessage(s, s1, s2, s3, s4);
                    }
                };
                try {
                    testBot.connect("irc.freenode.net");
                    testBot.joinChannel(CHANNEL);
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
}
