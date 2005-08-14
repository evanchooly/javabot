package javabot.operations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javabot.ApplicationException;
import javabot.BotEvent;
import javabot.Database;
import javabot.JDBCDatabase;
import javabot.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;

/**
 * Created Jun 28, 2005
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public class BaseOperationTest {
    private static Log log = LogFactory.getLog(BaseOperationTest.class);
    protected static final String SENDER = "cheeser";
    protected static final String CHANNEL = "#test";
    protected static final String LOGIN = "";
    protected static final String HOSTNAME = "localhost";
    public static final String OKAY = "Okay, " + SENDER + ".";
    public static final String ALREADY_HAVE_FACTOID = "I already have a factoid with that name, "
        + SENDER;
    private JDBCDatabase _jdbcDatabase;

    public BaseOperationTest() {
    }

    public BaseOperationTest(String name) {
    }

    protected void testOperation(String message, String response, String errorMessage) {
        testOperation(message, response, errorMessage, getOperation());
    }

    protected void testOperation(String message, String response, String errorMessage,
        BotOperation operation) {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, message);
        List<Message> results = operation.handleMessage(event);
        Assert.assertTrue(results.size() != 0);
        Message result = results.get(0);
        log.debug("result = " + result);
        Assert.assertEquals(response, result.getMessage(), errorMessage);
    }

    protected void testOperation(String message, String[] responses, String errorMessage) {
        testOperation(message, responses, errorMessage, getOperation());
    }

    protected void testOperation(String message, String[] responses, String errorMessage,
        BotOperation operation) {
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
        if(! success) {
            Assert.fail(errorMessage);
        }
    }

    protected BotOperation getOperation() {
        throw new ApplicationException("Implement this method on " + getClass().getName());
    }

    protected Database getDatabase() {
        if(_jdbcDatabase == null) {
            try {
                _jdbcDatabase = new JDBCDatabase();
            } catch(IOException e) {
                log.error(e.getMessage(), e);
                throw new ApplicationException(e.getMessage());
            }
        }
        return _jdbcDatabase;
    }

    public String getForgetMessage(String factoid) {
        return "I forgot about " + factoid + ", " + SENDER + ".";
    }

    protected String getFoundMessage(String factoid, String value) {
        return SENDER + ", " + factoid + " is " + value;
    }

    protected void forgetFactoid(String name) {
        testOperation("forget " + name, "I forgot about " + name + ", " + SENDER + ".",
            "I never knew about " + name + " anyway, " + SENDER + ".",
            new ForgetFactoidOperation(getDatabase()));
    }
}