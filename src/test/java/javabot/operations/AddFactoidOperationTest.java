package javabot.operations;

import java.io.IOException;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = {"operations"})
public class AddFactoidOperationTest extends BaseOperationTest {

    @Autowired
    private FactoidDao factoidDao;

    @Autowired
    private ChangeDao changeDao;

    public AddFactoidOperationTest() {
    }

    public AddFactoidOperationTest(final String name) {
        this();
    }

    @BeforeMethod
    public void setUp() {
        if (factoidDao.hasFactoid("test")) {
            factoidDao.delete(SENDER, "test");
        }
        if (factoidDao.hasFactoid("ping $1")) {
            factoidDao.delete(SENDER, "ping $1");
        }
        if (factoidDao.hasFactoid("what")) {
            factoidDao.delete(SENDER, "what");
        }
        if (factoidDao.hasFactoid("what up")) {
            factoidDao.delete(SENDER, "what up");
        }
        if (factoidDao.hasFactoid("test pong")) {
            factoidDao.delete(SENDER, "test pong");
        }
         if (factoidDao.hasFactoid("asdf")) {
            factoidDao.delete(SENDER, "asdf");
        }
         if (factoidDao.hasFactoid("12345")) {
            factoidDao.delete(SENDER, "12345");
        }
    }

    public void factoidAdd() {
        final String errorMessage = "Should have added the factoid";
        testOperation("test pong is pong", OKAY, errorMessage);
        testOperation("ping $1 is <action>sends some radar to $1, " + "awaits a response then forgets how long it took", OKAY, errorMessage);
        testOperation("what? is a question", OKAY, errorMessage);
        testOperation("what up? is <see>what?", OKAY, errorMessage);
    }

    @Test(dependsOnMethods = {"factoidAdd"})
    public void duplicateAdd() throws IOException {
        final String errorMessage = "Should not have added the factoid";
        final String message = "test pong is pong";
        testOperation(message, OKAY, errorMessage);
        testOperation(message, ALREADY_HAVE_FACTOID, errorMessage);
    }

    @Test(enabled = false)
    public void blankValue() {
        final String response = "Invalid factoid value";
        final String errorMessage = "Should not have added the factoid";
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "pong is");
        final List<Message> results = getOperation().handleMessage(event);
        System.out.println("results = " + results);
        Assert.assertTrue(!results.isEmpty());
        Assert.assertEquals(results.get(0).getMessage(), response);//, errorMessage);
    }

    public void addLog() {
        testOperation("12345 is 12345", OKAY, "Should have added the factoid.");
        Assert.assertTrue(changeDao.findLog(SENDER + " added '" + 12345 + "' with a value of '" + 12345 + "'"));
        forgetFactoid("12345");
    }

    public void channelMessage() throws IOException {
        final BotEvent event = new BotEvent("##javabot", SENDER, "", "localhost", "pong is");
        Assert.assertEquals(getOperation().handleChannelMessage(event).size(), 0, "Should be an empty list");
    }

    public void parensFactoids() {
        final String factoid = "should be the full (/hi there) factoid";
        testOperation("asdf is <reply>" + factoid, OKAY, "Should have added the factoid.");

        final GetFactoidOperation operation = new GetFactoidOperation(getJavabot());
        inject(operation);
        final String errorMessage = "Should have found the factoid";
        testOperation("asdf", factoid, errorMessage, operation);
    }

    @Override
    protected BotOperation createOperation() {
        return new AddFactoidOperation(getJavabot());
    }
}