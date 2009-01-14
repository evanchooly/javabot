package javabot.operations;

import java.util.List;

import javabot.BaseTest;
import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.unitils.spring.annotation.SpringApplicationContext;

@SpringApplicationContext("classpath:test-application-config.xml")
public abstract class BaseOperationTest extends BaseTest /*extends UnitilsTestNG*/ {
    @Autowired
    private FactoidDao factoidDao;
    @Autowired
    private ChangeDao changeDao;

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