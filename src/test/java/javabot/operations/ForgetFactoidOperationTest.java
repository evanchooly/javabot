package javabot.operations;

import java.io.IOException;

import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

@Test(groups = {"operations"})
public class ForgetFactoidOperationTest extends BaseOperationTest {
    @Autowired
    private FactoidDao factoidDao;
    @Autowired
    private ChangeDao changeDao;

    public void forgetFactoid() {
        if (!factoidDao.hasFactoid("afky")) {
            factoidDao.addFactoid(getTestBot().getNick(), "afky", "test");
        }
        testMessage("forget afky", "I forgot about afky, " + getTestBot().getNick() + ".");
    }

    public void nonexistantFactoid() {
        testMessage("forget asdfghjkl",
            String.format("I never knew about asdfghjkl anyway, %s.", getTestBot().getNick()));
    }

    public void channelMessage() throws IOException {
        testChannelMessage("pong is");
    }
}