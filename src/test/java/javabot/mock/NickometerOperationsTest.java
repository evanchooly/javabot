package javabot.mock;


import javabot.BotEvent;
import javabot.Message;
import javabot.operations.NickometerOperation;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;

import java.util.List;

/**
 * @author joed
 */

@Test(groups = {"operations"})
public class NickometerOperationsTest extends UnitilsTestNG {

    private static String CHANNEL = "#TEST";
    private static String SENDER = "joed";
    private static String LOGIN = "joed";
    private static String HOSTNAME = "localhost";

    private NickometerOperation nickOperation = new NickometerOperation();

    public void testNick() {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "nickometer MAGNIFICENT");
        List<Message> results = nickOperation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "The nick MAGNIFICENT is 95% lame.");
    }

    public void testNickCh33s3r() {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "nickometer ch33s3r");
        List<Message> results = nickOperation.handleMessage(event);
        Assert.assertEquals(results.get(0).getMessage(), "The nick ch33s3r is 65% lame.");
    }
}