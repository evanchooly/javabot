package javabot.mock;


import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.operations.SayOperation;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;

/**
 * @author joed
 */

@Test(groups = {"operations"})
public class SayOperationsTest extends UnitilsTestNG {

    private static final String CHANNEL = "#TEST";
    private static final String SENDER = "joed";
    private static final String LOGIN = "joed";
    private static final String HOSTNAME = "localhost";

    private SayOperation sayOperation = new SayOperation(new Javabot());

    public void testSay() {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "say MAGNIFICENT");
        List<Message> results = sayOperation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "MAGNIFICENT");
    }
}