package javabot.operations;

import javabot.dao.ChangeDao;
import javabot.dao.KarmaDao;
import javabot.dao.SeenDao;
import javabot.model.Karma;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

@Test(groups = {"operations"})
public class KarmaChangeOperationTest extends BaseOperationTest {
    private static final Log log = LogFactory.getLog(KarmaChangeOperationTest.class);

    @SpringBeanByType
    private KarmaDao karmaDao;

    @SpringBeanByType
    private ChangeDao changeDao;
    @SpringBeanByType
    private SeenDao seenDao;

    public void updateKarma() {
        seenDao.logSeen("testjavabot", CHANNEL, "dude!");
        Karma karma = karmaDao.find("testjavabot");
        int value = karma != null ? karma.getValue() : 0;
        testOperation("testjavabot++", "testjavabot has a karma level of " + (value++) + ", " + SENDER, "");
        testOperation("testjavabot++", "testjavabot has a karma level of " + (value++) + ", " + SENDER, "");
        testOperation("testjavabot--", "testjavabot has a karma level of " + (value--) + ", " + SENDER, "");
        testOperation("testjavabot--", "testjavabot has a karma level of " + (value--) + ", " + SENDER, "");
        testOperation("testjavabot--", "testjavabot has a karma level of " + (value--) + ", " + SENDER, "");
        testOperation("testjavabot++", "testjavabot has a karma level of " + (value++) + ", " + SENDER, "");
    }

    public void logNew() {
        log.debug("BEGIN ADDLOG");
        String target = "karmachange2";
        testOperation(target + "++", target + " has a karma level of 1, " + SENDER, "");
        String message = SENDER + " added 'karma " + target + "' with " + "a value of '1'";
        log.debug("looking for " + message);
        Assert.assertTrue(changeDao.findLog(message));
        forgetFactoid2("karma " + target);
        log.debug("END ADDLOG");
    }

    public void logChanged() {
        log.debug("BEGIN LOGCHANGED");
        String target = "javabot";
        int karma = getKarma(target) + 1;
        testOperation(target + "++", target + " has a karma level of " + karma + ", " + SENDER, "");
        String message = SENDER + " changed 'karma " + target + "' to" + " '" + karma + "'";
        log.debug("looking for " + message);
        Assert.assertTrue(changeDao.findLog(message));
        log.debug("END LOGCHANGED");
    }

    public void changeOwnKarma() {
        int karma = getKarma(SENDER);
        testOperation(SENDER + "++", new String[]{"Changing one's own karma is not permitted.", "You now have a karma level of " + (karma - 1)}, "");
        int karma2 = getKarma(SENDER);
        Assert.assertTrue(karma2 == karma - 1, "Should have lost one karma point.");
    }

    private int getKarma(String target) {
        log.debug("target = " + target);
        Karma karma = karmaDao.find(target);
        log.debug("value = " + karma.getValue());

        return karma.getValue();
    }

    @Override
    protected BotOperation getOperation() {

        KarmaChangeOperation operation = null;
        try {
            operation = new KarmaChangeOperation(karmaDao, seenDao);
            return operation;
        } catch (Exception e) {
            Assert.fail("Could not create operation: " + e.getMessage());
        }

        return operation;
    }
}
