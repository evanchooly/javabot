package javabot.operations;

import java.io.IOException;
import java.util.List;

import javabot.BaseTest;
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

    @BeforeMethod
    public void setUp() {
        factoidDao.delete(BaseTest.TEST_USER, "test");
        factoidDao.delete(BaseTest.TEST_USER, "ping $1");
        factoidDao.delete(BaseTest.TEST_USER, "what");
        factoidDao.delete(BaseTest.TEST_USER, "what up");
        factoidDao.delete(BaseTest.TEST_USER, "test pong");
        factoidDao.delete(BaseTest.TEST_USER, "asdf");
        factoidDao.delete(BaseTest.TEST_USER, "12345");
        factoidDao.delete(BaseTest.TEST_USER, "replace");
    }

    public void factoidAdd() {
        testMessage("~test pong is pong", ok);
        testMessage("~ping $1 is <action>sends some radar to $1, awaits a response then forgets how long it took",
            ok);
        testMessage("~what? is a question", ok);
        testMessage("~what up? is <see>what?", ok);
    }

    public void replace() {
        testMessage("~replace is first entry", ok);
        testMessage("~no, replace is <reply>second entry", "OK, " + BaseTest.TEST_USER + ".");
        testMessage("~replace", "second entry");
        forgetFactoid("replace");
        testMessage("~no, replace is <reply>second entry", "OK, " + BaseTest.TEST_USER + ".");
    }

    @Test(dependsOnMethods = {"factoidAdd"})
    public void duplicateAdd() throws IOException {
        final String message = "~test pong is pong";
        testMessage(message, ok);
        testMessage(message, String.format("I already have a factoid named %s, %s", "test pong", BaseTest.TEST_USER));
        forgetFactoid("test pong");
    }

    @Test(enabled = false)
    public void blankValue() {
        testMessage("~pong is", "Invalid factoid value");
    }

    public void addLog() {
        testMessage("~12345 is 12345", ok);
        Assert.assertTrue(
            changeDao.findLog(String.format("%s added '12345' with a value of '12345'", BaseTest.TEST_USER)));
        forgetFactoid("12345");
    }

    public void parensFactoids() {
        final String factoid = "should be the full (/hi there) factoid";
        testMessage("~asdf is <reply>" + factoid, ok);
        testMessage("~asdf", factoid);
    }
}