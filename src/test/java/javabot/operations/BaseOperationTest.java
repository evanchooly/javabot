package javabot.operations;

import java.util.List;

import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;
import org.unitils.UnitilsTestNG;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import javabot.dao.FactoidDao;
import javabot.dao.ChangeDao;
import javabot.BotEvent;
import javabot.Message;
import javabot.ApplicationException;

@SpringApplicationContext("test-application-config.xml")
public class BaseOperationTest extends UnitilsTestNG {
    private static final Log log = LogFactory.getLog(BaseOperationTest.class);
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

    protected void testOperation2(String message, String response, String errorMessage) {
        testOperation2(message, response, errorMessage, getOperation2());
    }

    protected void testOperation2(String message, String response, String errorMessage, BotOperation operation) {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, message);
        List<Message> results = operation.handleMessage(event);
        Assert.assertTrue(!results.isEmpty());
        Message result = results.get(0);
        log.debug("result = " + result);
        Assert.assertEquals(response, result.getMessage(), errorMessage);
    }

    protected void testOperation2(String message, String[] responses, String errorMessage) {
        testOperation2(message, responses, errorMessage, getOperation2());
    }

    protected void testOperation2(String message, String[] responses, String errorMessage, BotOperation operation) {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, message);
        List<Message> results = operation.handleMessage(event);
        Message result = results.get(0);
        boolean success = false;
        for (String response : responses) {
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

    protected BotOperation getOperation2() {
        throw new ApplicationException("Implement this method on " + getClass().getName());
    }


    public String getForgetMessage2(String factoid) {
        return "I forgot about " + factoid + ", " + SENDER + ".";
    }

    protected String getFoundMessage2(String factoid, String value) {
        return SENDER + ", " + factoid + " is " + value;
    }

    protected void forgetFactoid2(String name) {
        testOperation2("forget " + name, "I forgot about " + name + ", " + SENDER + ".", "I never knew about " + name + " anyway, " + SENDER
            + ".", new ForgetFactoidOperation(factoidDao, changeDao));
    }
}
