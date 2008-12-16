package javabot.mock;

import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.operations.NickometerOperation;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;

/**
 * @author joed
 */

@Test(groups = {"operations"})
public class NickometerOperationsTest extends UnitilsTestNG {

    private static final String CHANNEL = "#TEST";
    private static final String SENDER = "joed";
    private static final String LOGIN = "joed";
    private static final String HOSTNAME = "localhost";

    private NickometerOperation nickOperation = new NickometerOperation(new Javabot());

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