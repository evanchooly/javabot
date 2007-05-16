package javabot.mock;


import javabot.BotEvent;
import javabot.Message;
import javabot.operations.SayOperation;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;

import java.util.List;

/**
 * @author joed
 */

@Test(groups = {"operations"})
public class SayOperationsTest extends UnitilsTestNG {

    private static String CHANNEL = "#TEST";
    private static String SENDER = "joed";
    private static String LOGIN = "joed";
    private static String HOSTNAME = "localhost";

    private SayOperation sayOperation = new SayOperation();

    public void testSay() {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "say MAGNIFICENT");
        List<Message> results = sayOperation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "MAGNIFICENT");
    }
}