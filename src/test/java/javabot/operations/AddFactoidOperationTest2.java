package javabot.operations;

import javabot.BotEvent;
import javabot.dao.ChangesDao;
import javabot.dao.FactoidDao;
import org.testng.Assert;
import org.testng.annotations.Configuration;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

import java.io.IOException;

@Test(groups = {"operations"})
public class AddFactoidOperationTest2 extends BaseOperationTest2 {

    @SpringBeanByType
    private FactoidDao factoidDao;

    @SpringBeanByType
    private ChangesDao changesDao;

    public AddFactoidOperationTest2() {
        super();
    }

    public AddFactoidOperationTest2(String name) {
        super(name);
    }

    @Configuration(beforeTestMethod = true)
    public void setUp() {
        if (factoidDao.hasFactoid("test")) {
            factoidDao.forgetFactoid(SENDER, "test", changesDao, "test");
        }
        if (factoidDao.hasFactoid("ping $1")) {
            factoidDao.forgetFactoid(SENDER, "ping $1", changesDao, "test");
        }
        if (factoidDao.hasFactoid("what")) {
            factoidDao.forgetFactoid(SENDER, "what", changesDao, "test");
        }
        if (factoidDao.hasFactoid("what up")) {
            factoidDao.forgetFactoid(SENDER, "what up", changesDao, "test");
        }
        if (factoidDao.hasFactoid("test pong")) {
            factoidDao.forgetFactoid(SENDER, "test pong", changesDao, "test");
        }
         if (factoidDao.hasFactoid("asdf")) {
            factoidDao.forgetFactoid(SENDER, "asdf", changesDao, "test");
        }
         if (factoidDao.hasFactoid("12345")) {
            factoidDao.forgetFactoid(SENDER, "12345", changesDao, "test");
        }

    }

    public void factoidAdd() {


        

        String errorMessage = "Should have added the factoid";
        testOperation2("test pong is pong", OKAY, errorMessage);
        testOperation2("ping $1 is <action>sends some radar to $1, " + "awaits a response then forgets how long it took", OKAY, errorMessage);
        testOperation2("what? is a question", OKAY, errorMessage);
        testOperation2("what up? is <see>what?", OKAY, errorMessage);
    }

    @Test(dependsOnMethods = {"factoidAdd"})
    public void duplicateAdd() throws IOException {

        String errorMessage = "Should not have added the factoid";

        String message = "test pong is pong";

        testOperation2(message, OKAY, errorMessage);

        testOperation2(message, ALREADY_HAVE_FACTOID, errorMessage);
    }

    public void blankFactoid() throws IOException {
        String response = "Invalid factoid name";
        String errorMessage = "Should not have added the factoid";
        testOperation2("is pong", response, errorMessage);
    }

    public void blankValue() {
        String response = "Invalid factoid value";
        String errorMessage = "Should not have added the factoid";
        testOperation2("pong is", response, errorMessage);
    }

    public void addLog() {

        testOperation2("12345 is 12345", OKAY, "Should have added the factoid.");
        Assert.assertTrue(changesDao.findLog(SENDER + " added '" + 12345 + "' with a value of '" + 12345 + "'"));
        forgetFactoid2("12345");
    }

    public void channelMessage() throws IOException {
        BotEvent event = new BotEvent("#test", SENDER, "", "localhost", "pong is");
        Assert.assertEquals(getOperation2().handleChannelMessage(event).size(), 0, "Should be an empty list");
    }

    public void parensFactoids() {
        String factoid = "should be the full (/hi there) factoid";
        testOperation2("asdf is <reply>" + factoid, OKAY, "Should have added the factoid.");

        GetFactoidOperation operation = new GetFactoidOperation(factoidDao);
        String errorMessage = "Should have found the factoid";
        testOperation2("asdf", factoid, errorMessage, operation);
    }

    protected AddFactoidOperation getOperation2() {
        return new AddFactoidOperation(factoidDao, this.changesDao, "test");
    }

}