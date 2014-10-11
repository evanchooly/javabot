package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.IOException;

@Test(groups = {"operations"})
public class AddFactoidOperationTest extends BaseOperationTest {
    @Inject
    private FactoidDao factoidDao;
    @Inject
    private ChangeDao changeDao;

    @BeforeMethod
    public void setUp() {
        factoidDao.delete(TEST_NICK, "test");
        factoidDao.delete(TEST_NICK, "ping $1");
        factoidDao.delete(TEST_NICK, "what");
        factoidDao.delete(TEST_NICK, "what up");
        factoidDao.delete(TEST_NICK, "test pong");
        factoidDao.delete(TEST_NICK, "asdf");
        factoidDao.delete(TEST_NICK, "12345");
        factoidDao.delete(TEST_NICK, "replace");
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
        testMessage("~no, replace is <reply>second entry", "OK, " + getTestUser() + ".");
        testMessage("~replace", "second entry");
        forgetFactoid("replace");
        testMessage("~no, replace is <reply>second entry", "OK, " + getTestUser() + ".");
    }

    @Test(dependsOnMethods = {"factoidAdd"})
    public void duplicateAdd() throws IOException {
        final String message = "~test pong is pong";
        testMessage(message, ok);
        testMessage(message, Sofia.factoidExists("test pong", getTestUser()));
        forgetFactoid("test pong");
    }

    @Test
    public void blankValue() {
        testMessage("~pong is", Sofia.unhandledMessage(getTestUser().getNick()));
    }

    public void addLog() {
        testMessage("~12345 is 12345", ok);
        Assert.assertTrue(changeDao.findLog(Sofia.factoidAdded(getTestUser().getNick(), "12345", "12345")));
        forgetFactoid("12345");
    }

    public void parensFactoids() {
        final String factoid = "should be the full (/hi there) factoid";
        testMessage("~asdf is <reply>" + factoid, ok);
        testMessage("~asdf", factoid);
    }
}