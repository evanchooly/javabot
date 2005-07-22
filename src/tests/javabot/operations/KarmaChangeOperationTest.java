package javabot.operations;

import java.util.List;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javabot.BotEvent;
import javabot.Message;

/**
 * Created Jul 22, 2005
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test(groups = {"operations"})
public class KarmaChangeOperationTest extends BaseOperationTest {
    private static Log log = LogFactory.getLog(KarmaChangeOperationTest.class);

    public void updateKarma() {
        testOperation("testjavabot++", "testjavabot has a karma level of 1, cheeser", "");
        testOperation("testjavabot++", "testjavabot has a karma level of 2, cheeser", "");
        testOperation("testjavabot--", "testjavabot has a karma level of 1, cheeser", "");
        testOperation("testjavabot--", "testjavabot has a karma level of 0, cheeser", "");
        testOperation("testjavabot--", "testjavabot has a karma level of -1, cheeser", "");
        testOperation("testjavabot++", "testjavabot has a karma level of 0, cheeser", "");
    }

    public void changeOwnKarma() {
        int karma = getKarma(SENDER);
        testOperation(SENDER + "++", new String[] {
            "Changing one's own karma is not permitted.",
            "You now have a karma level of " + (karma-1) }, "");
        int karma2 = getKarma(SENDER);
        Assert.assertTrue(karma2 == karma -1, "Should have lost one karma point.");
    }

    private int getKarma(String sender) {
        KarmaReadOperation operation = new KarmaReadOperation(getDatabase());
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "karma " + sender);
        List<Message> results = operation.handleMessage(event);
        log.debug("results = " + results);
        String[] message = results.get(0).getMessage().split(" ");
        String value = message[7].split("\\.")[0];
        log.debug("value = " + value);

        return Integer.parseInt(value);
    }

    protected BotOperation getOperation() {
        return new KarmaChangeOperation(getDatabase());
    }
}
