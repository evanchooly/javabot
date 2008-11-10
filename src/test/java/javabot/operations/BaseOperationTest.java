package javabot.operations;

import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.unitils.UnitilsTestNG;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

@SpringApplicationContext("classpath:test-application-config.xml")
public abstract class BaseOperationTest extends UnitilsTestNG {
    protected static final String SENDER = "cheeser";
    protected static final String CHANNEL = "#test";
    protected static final String LOGIN = "";
    protected static final String HOSTNAME = "localhost";
    public static final String OKAY = "Okay, " + SENDER + ".";
    public static final String ALREADY_HAVE_FACTOID = "I already have a factoid with that name, " + SENDER;
    @SpringApplicationContext
    ApplicationContext springApplicationContext;
    @SpringBeanByType
    private FactoidDao factoidDao;
    @SpringBeanByType
    private ChangeDao changeDao;

    public BaseOperationTest() {
    }

    public BaseOperationTest(String name) {
    }
    
    protected void testOperation(String message, String response, String errorMessage) {
        testOperation(message, response, errorMessage, getOperation());
    }

    protected void testOperation(String message, String response, String errorMessage, BotOperation operation) {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, message);
        List<Message> results = operation.handleMessage(event);
        Assert.assertTrue(!results.isEmpty());
        Assert.assertEquals(results.get(0).getMessage(), response, errorMessage);
    }

    protected void testOperation(String message, String[] responses, String errorMessage) {
        testOperation(message, responses, errorMessage, getOperation());
    }

    protected void testOperation(String message, String[] responses, String errorMessage, BotOperation operation) {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, message);
        List<Message> results = operation.handleMessage(event);
        Message result = results.get(0);
        boolean success = false;
        for(String response : responses) {
            try {
                Assert.assertEquals(response, result.getMessage(), errorMessage);
                success = true;
            } catch(AssertionError ae) {
                // try next
            }
        }
        if(!success) {
            Assert.fail(errorMessage);
        }
    }

    protected abstract BotOperation getOperation();

    public String getForgetMessage(String factoid) {
        return "I forgot about " + factoid + ", " + SENDER + ".";
    }

    protected String getFoundMessage(String factoid, String value) {
        return SENDER + ", " + factoid + " is " + value;
    }

    protected void forgetFactoid(String name) {
        testOperation("forget " + name, "I forgot about " + name + ", " + SENDER + ".",
            "I never knew about " + name + " anyway, " + SENDER
                + ".", new ForgetFactoidOperation(factoidDao, changeDao));
    }
}
