package javabot.operations;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;
import org.testng.Assert;
import javabot.BotEvent;

/**
 * Created Jun 30, 2005
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
@Test(groups = {"operations"})
public class ForgetFactoidOperationTest extends BaseOperationTest {
    private static Log log = LogFactory.getLog(ForgetFactoidOperationTest.class);

    public void forgetFactoid() {
        testOperation("forget afk", "I forgot about afk, " + SENDER + ".",
            "Should have forgotten factoid");
    }

    public void nonexistantFactoid() {
        testOperation("forget asdfghjkl", "I never knew about asdfghjkl anyway, "
            + SENDER + ".", "Should not have known about factoid");
    }

    public void addLog() {
        forgetFactoid("gprs");
        Assert.assertTrue(getDatabase().findLog(SENDER + " removed 'gprs'"));
    }

    protected BotOperation getOperation() {
        return new ForgetFactoidOperation(getDatabase());
    }

    public void channelMessage() throws IOException {
        BotEvent event = new BotEvent("#test", SENDER, "", "localhost", "pong is");
        Assert.assertEquals(getOperation().handleChannelMessage(event).size(), 0,
            "Should be an empty list");
    }
}