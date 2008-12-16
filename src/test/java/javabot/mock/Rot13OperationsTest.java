package javabot.mock;


import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.operations.Rot13Operation;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;

/**
 * @author joed
 */

@Test(groups = {"operations"})
public class Rot13OperationsTest extends UnitilsTestNG {

    private static final String CHANNEL = "#TEST";
    private static final String SENDER = "joed";
    private static final String LOGIN = "joed";
    private static final String HOSTNAME = "localhost";

    private Rot13Operation rot13Operation = new Rot13Operation(new Javabot());

    public void testSay() {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "rot13 MAGNIFICENT");
        List<Message> results = rot13Operation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "ZNTAVSVPRAG");
    }
}