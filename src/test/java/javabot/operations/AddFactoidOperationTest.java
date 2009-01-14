package javabot.operations;

import java.io.IOException;

import javabot.BotEvent;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

@Test(groups = {"operations"}, enabled=false)
public class AddFactoidOperationTest extends BaseOperationTest {

    @SpringBeanByType
    private FactoidDao factoidDao;

    @SpringBeanByType
    private ChangeDao changeDao;

    public AddFactoidOperationTest() {
    }

    public AddFactoidOperationTest(String name) {
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
        String errorMessage = "Should have added the factoid";
        testOperation("test pong is pong", OKAY, errorMessage);
        testOperation("ping $1 is <action>sends some radar to $1, " + "awaits a response then forgets how long it took", OKAY, errorMessage);
        testOperation("what? is a question", OKAY, errorMessage);
        testOperation("what up? is <see>what?", OKAY, errorMessage);
    }

    @Test(dependsOnMethods = {"factoidAdd"})
    public void duplicateAdd() throws IOException {
        String errorMessage = "Should not have added the factoid";
        String message = "test pong is pong";
        testOperation(message, OKAY, errorMessage);
        testOperation(message, ALREADY_HAVE_FACTOID, errorMessage);
    }

    public void blankValue() {
        String response = "Invalid factoid value";
        String errorMessage = "Should not have added the factoid";
        testOperation("pong is", response, errorMessage);
    }

    public void addLog() {
        testOperation("12345 is 12345", OKAY, "Should have added the factoid.");
        Assert.assertTrue(changeDao.findLog(SENDER + " added '" + 12345 + "' with a value of '" + 12345 + "'"));
        forgetFactoid("12345");
    }

    public void channelMessage() throws IOException {
        BotEvent event = new BotEvent("#test", SENDER, "", "localhost", "pong is");
        Assert.assertEquals(getOperation().handleChannelMessage(event).size(), 0, "Should be an empty list");
    }

    public void parensFactoids() {
        String factoid = "should be the full (/hi there) factoid";
        testOperation("asdf is <reply>" + factoid, OKAY, "Should have added the factoid.");

        GetFactoidOperation operation = new GetFactoidOperation(getJavabot());
        String errorMessage = "Should have found the factoid";
        testOperation("asdf", factoid, errorMessage, operation);
    }

    @Override
    protected BotOperation createOperation() {
        return new AddFactoidOperation(getJavabot());
    }
}