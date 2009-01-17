package javabot.operations;

import java.io.IOException;

import javabot.BotEvent;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"operations"})
public class ForgetFactoidOperationTest extends BaseOperationTest {
    @Autowired
    private FactoidDao factoidDao;

    @Autowired
    private ChangeDao changeDao;

    public void forgetFactoid() {
        if (!factoidDao.hasFactoid("afky")) {
            factoidDao.addFactoid(SENDER, "afky", "test");
        }
        testOperation("forget afky", "I forgot about afky, " + SENDER + ".", "Should have forgotten factoid");
    }

    public void nonexistantFactoid() {
        testOperation("forget asdfghjkl", "I never knew about asdfghjkl anyway, " + SENDER + ".", "Should not have known about factoid");
    }

    @Override
    protected BotOperation createOperation() {
        return new ForgetFactoidOperation(getJavabot());
    }

    public void channelMessage() throws IOException {
        final BotEvent event = new BotEvent("##javabot", SENDER, "", "localhost", "pong is");
        Assert.assertEquals(getOperation().handleChannelMessage(event).size(), 0, "Should be an empty list");
    }
}