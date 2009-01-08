package javabot.operations;

import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import org.jibble.pircbot.PircBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;
import org.unitils.spring.annotation.SpringApplicationContext;

@SpringApplicationContext("classpath:test-application-config.xml")
public abstract class BaseOperationTest /*extends UnitilsTestNG*/ {
    protected static final String SENDER = "cheeser";
    protected static final String CHANNEL = "##javabot";
    protected static final String LOGIN = "";
    protected static final String HOSTNAME = "localhost";
    public static final String OKAY = "Okay, " + SENDER + ".";
    public static final String ALREADY_HAVE_FACTOID = "I already have a factoid with that name, " + SENDER;
    @Autowired
    private FactoidDao factoidDao;
    @Autowired
    private ChangeDao changeDao;
    private Javabot bot;
    private PircBot testBot;
    private ApplicationContext context;

    public BaseOperationTest() {
        context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        createBot();
        inject(this);
    }

    public BaseOperationTest(final String name) {
        this();
    }

    protected final Javabot createBot() {
        if (bot != null) {
            bot.disconnect();
            bot.dispose();
        }
        bot = new Javabot(context);
        bot.joinChannel("##javabot");
        return bot;
    }

    public Javabot getJavabot() {
        if (bot == null) {
            createBot();
        }
        return bot;
    }

    public PircBot getTestBot() {
        if (testBot == null) {
            testBot = new PircBot() {
                {
                    setName("javabot" + System.currentTimeMillis());
                }

                @Override
                protected void onMessage(final String s, final String s1, final String s2, final String s3, final String s4) {
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

    protected void testOperation(final String message, final String response, final String errorMessage) {
        testOperation(message, response, errorMessage, getOperation());
    }

    protected void testOperation(final String message, final String response, final String errorMessage,
        final BotOperation operation) {
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, message);
        final List<Message> results = operation.handleMessage(event);
        Assert.assertTrue(!results.isEmpty());
        Assert.assertEquals(results.get(0).getMessage(), response, errorMessage);
    }

    protected void testOperation(final String message, final String[] responses, final String errorMessage) {
        testOperation(message, responses, errorMessage, getOperation());
    }

    protected void testOperation(final String message, final String[] responses, final String errorMessage,
        final BotOperation operation) {
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, message);
        final List<Message> results = operation.handleMessage(event);
        final Message result = results.get(0);
        boolean success = false;
        for (final String response : responses) {
            try {
                Assert.assertEquals(response, result.getMessage(), errorMessage);
                success = true;
            } catch (AssertionError ae) {
                // try next
            }
        }
        if (!success) {
            Assert.fail(errorMessage);
        }
    }

    protected final BotOperation getOperation() {
        final BotOperation operation = createOperation();
        inject(operation);
        return operation;
    }

    private void inject(final Object object) {
        context.getAutowireCapableBeanFactory().autowireBean(object);
    }

    protected abstract BotOperation createOperation();

    public String getForgetMessage(final String factoid) {
        return "I forgot about " + factoid + ", " + SENDER + ".";
    }

    protected String getFoundMessage(final String factoid, final String value) {
        return SENDER + ", " + factoid + " is " + value;
    }

    protected void forgetFactoid(final String name) {
        testOperation("forget " + name, "I forgot about " + name + ", " + SENDER + ".",
            "I never knew about " + name + " anyway, " + SENDER
                + ".", new ForgetFactoidOperation(getJavabot()));
    }
}