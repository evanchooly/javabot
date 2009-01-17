package javabot.mock;

import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.operations.BaseOperationTest;
import javabot.operations.BotOperation;
import javabot.operations.NickometerOperation;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author joed
 */

@Test(groups = {"operations"})
public class NickometerOperationsTest extends BaseOperationTest {

    private static final String CHANNEL = "##javabot";
    private static final String SENDER = "joed";
    private static final String LOGIN = "joed";
    private static final String HOSTNAME = "localhost";

    public BotOperation createOperation() {
        return new NickometerOperation(getJavabot());
    }

    public void testNick() {
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "nickometer MAGNIFICENT");
        final List<Message> results = getOperation().handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "The nick MAGNIFICENT is 95% lame.");
    }

    public void testNickCh33s3r() {
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "nickometer ch33s3r");
        final List<Message> results = getOperation().handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "The nick ch33s3r is 65% lame.");
    }
}