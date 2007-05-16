package javabot.operations;

import javabot.Factoid;
import javabot.Javabot;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created Jul 22, 2005
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test(groups = {"operations"})
public class KarmaChangeOperationTest extends BaseOperationTest {
    private static final Log log = LogFactory.getLog(KarmaChangeOperationTest.class);

    public void updateKarma() {
        testOperation("testjavabot++", "testjavabot has a karma level of 1, " + SENDER, "");
        testOperation("testjavabot++", "testjavabot has a karma level of 2, " + SENDER, "");
        testOperation("testjavabot--", "testjavabot has a karma level of 1, " + SENDER, "");
        testOperation("testjavabot--", "testjavabot has a karma level of 0, " + SENDER, "");
        testOperation("testjavabot--", "testjavabot has a karma level of -1, " + SENDER, "");
        testOperation("testjavabot++", "testjavabot has a karma level of 0, " + SENDER, "");
    }

    public void logNew() {
        log.debug("BEGIN ADDLOG");
        String target = "karmachange";
        testOperation(target + "++", target + " has a karma level of 1, " + SENDER, "");
        String message = SENDER + " added 'karma " + target + "' with "
            + "a value of '1'";
        log.debug("looking for " + message);
        Assert.assertTrue(getDatabase().findLog(message));
        forgetFactoid("karma " + target);
        log.debug("END ADDLOG");
    }

    public void logChanged() {
        log.debug("BEGIN LOGCHANGED");
        String target = "javabot";
        int karma = getKarma(target) + 1;
        testOperation(target + "++", target + " has a karma level of " + karma
            + ", " + SENDER, "");
        String message = SENDER + " changed 'karma " + target + "' to"
            + " '" + karma + "'";
        log.debug("looking for " + message);
        Assert.assertTrue(getDatabase().findLog(message));
        log.debug("END LOGCHANGED");
    }

    public void changeOwnKarma() {
        int karma = getKarma(SENDER);
        testOperation(SENDER + "++", new String[] {
            "Changing one's own karma is not permitted.",
            "You now have a karma level of " + (karma-1) }, "");
        int karma2 = getKarma(SENDER);
        Assert.assertTrue(karma2 == karma -1, "Should have lost one karma point.");
    }

    private int getKarma(String target) {
        log.debug("target = " + target);
        Factoid factoid = getDatabase().getFactoid("karma " + target);
        String value = factoid.getValue();
        log.debug("value = " + value);

        return Integer.parseInt(value);
    }

    @Override
    protected BotOperation getOperation() {
        KarmaChangeOperation operation = null;
        try {
            operation = new KarmaChangeOperation(getDatabase(), new Javabot());
            return operation;
        } catch(Exception e) {
            Assert.fail("Could not create operation");
        }

        return operation;
    }
}
