package javabot.operations;

import java.io.IOException;

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
        factoidDao.delete(getTestBot().getNick(), "test");
        factoidDao.delete(getTestBot().getNick(), "ping $1");
        factoidDao.delete(getTestBot().getNick(), "what");
        factoidDao.delete(getTestBot().getNick(), "what up");
        factoidDao.delete(getTestBot().getNick(), "test pong");
        factoidDao.delete(getTestBot().getNick(), "asdf");
        factoidDao.delete(getTestBot().getNick(), "12345");
        factoidDao.delete(getTestBot().getNick(), "replace");
    }

    public void factoidAdd() {
        testMessage("test pong is pong", ok);
        testMessage("ping $1 is <action>sends some radar to $1, awaits a response then forgets how long it took",
            ok);
        testMessage("what? is a question", ok);
        testMessage("what up? is <see>what?", ok);
    }

    public void replace() {
        testMessage("replace is first entry", ok);
        final TestBot bot = getTestBot();
        bot.sendMessage(getJavabotChannel(), getJavabot().getNick() + " no, replace is <reply>second entry");
        waitForResponses(bot, 1);
        Assert.assertEquals(bot.getOldestResponse().getMessage(), "OK, " + bot.getNick() + ".");
        testMessage("replace", "second entry");
        forgetFactoid("replace");
        bot.sendMessage(getJavabotChannel(), getJavabot().getNick() + " no, replace is <reply>second entry");
        waitForResponses(bot, 1);
        Assert.assertEquals(bot.getOldestResponse().getMessage(), "OK, " + bot.getNick() + ".");
    }

    @Test(dependsOnMethods = {"factoidAdd"})
    public void duplicateAdd() throws IOException {
        final String message = "test pong is pong";
        testMessage(message, ok);
        testMessage(message, String.format("I already have a factoid named %s, %s", "test pong", getTestBot().getNick()));
        forgetFactoid("test pong");
    }

    @Test(enabled = false)
    public void blankValue() {
        testMessage("pong is", "Invalid factoid value");
    }

    public void addLog() {
        testMessage("12345 is 12345", ok);
        Assert.assertTrue(changeDao.findLog(
            String.format("%s added '12345' with a value of '12345'", getTestBot().getNick())));
        forgetFactoid("12345");
    }

    public void channelMessage() throws IOException {
        testChannelMessage("pong is");
    }

    public void parensFactoids() {
        final String factoid = "should be the full (/hi there) factoid";
        testMessage("asdf is <reply>" + factoid, ok);
        testMessage("asdf", factoid);
    }
}