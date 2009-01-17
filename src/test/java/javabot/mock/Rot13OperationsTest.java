package javabot.mock;

import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.operations.BaseOperationTest;
import javabot.operations.BotOperation;
import javabot.operations.Rot13Operation;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author joed
 */

@Test(groups = {"operations"})
public class Rot13OperationsTest extends BaseOperationTest {

    private static final String CHANNEL = "##javabot";
    private static final String SENDER = "joed";
    private static final String LOGIN = "joed";
    private static final String HOSTNAME = "localhost";

    public BotOperation createOperation() {
        return new Rot13Operation(getJavabot());
    }

    public void testSay() {
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "rot13 MAGNIFICENT");
        final List<Message> results = getOperation().handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "ZNTAVSVPRAG");
    }
}