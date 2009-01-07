package javabot.mock;

import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.BaseServiceTest;
import javabot.operations.NickometerOperation;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author joed
 */

@Test(groups = {"operations"})
public class NickometerOperationsTest extends BaseServiceTest {

    private static final String CHANNEL = "#TEST";
    private static final String SENDER = "joed";
    private static final String LOGIN = "joed";
    private static final String HOSTNAME = "localhost";

    private NickometerOperation nickOperation = new NickometerOperation(getJavabot());

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