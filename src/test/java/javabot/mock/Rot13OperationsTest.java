package javabot.mock;


import javabot.BotEvent;
import javabot.Message;
import javabot.operations.Rot13Operation;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;

import java.util.List;

/**
 * @author joed
 */

@Test(groups = {"operations"})
public class Rot13OperationsTest extends UnitilsTestNG {

    private static String CHANNEL = "#TEST";
    private static String SENDER = "joed";
    private static String LOGIN = "joed";
    private static String HOSTNAME = "localhost";

    private Rot13Operation rot13Operation = new Rot13Operation();

    public void testSay() {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "rot13 MAGNIFICENT");
        List<Message> results = rot13Operation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "ZNTAVSVPRAG");
    }
}