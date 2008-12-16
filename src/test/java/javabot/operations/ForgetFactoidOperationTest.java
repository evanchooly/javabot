package javabot.operations;

import java.io.IOException;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

@Test(groups = {"operations"})
public class ForgetFactoidOperationTest extends BaseOperationTest {
    @SpringBeanByType
    private FactoidDao factoidDao;

    @SpringBeanByType
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
    protected BotOperation getOperation() {
        return new ForgetFactoidOperation(new Javabot(), factoidDao);
    }

    public void channelMessage() throws IOException {
        BotEvent event = new BotEvent("#test", SENDER, "", "localhost", "pong is");
        Assert.assertEquals(getOperation().handleChannelMessage(event).size(), 0, "Should be an empty list");
    }
}