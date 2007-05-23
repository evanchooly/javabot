package javabot.operations;

import javabot.Javabot;
import javabot.dao.ChangesDao;
import javabot.dao.FactoidDao;
import javabot.dao.model.Factoid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;


@Test(groups = {"operations"})
public class KarmaChangeOperationTest2 extends BaseOperationTest2 {
    private static final Log log = LogFactory.getLog(KarmaChangeOperationTest2.class);

    @SpringBeanByType
    private FactoidDao factoidDao;

    @SpringBeanByType
    private ChangesDao changesDao;

    public void updateKarma() {
        testOperation2("testjavabot++", "testjavabot has a karma level of 1, " + SENDER, "");
        testOperation2("testjavabot++", "testjavabot has a karma level of 2, " + SENDER, "");
        testOperation2("testjavabot--", "testjavabot has a karma level of 1, " + SENDER, "");
        testOperation2("testjavabot--", "testjavabot has a karma level of 0, " + SENDER, "");
        testOperation2("testjavabot--", "testjavabot has a karma level of -1, " + SENDER, "");
        testOperation2("testjavabot++", "testjavabot has a karma level of 0, "  + SENDER, "");
    }

    public void logNew() {
        log.debug("BEGIN ADDLOG");
        String target = "karmachange2";
        testOperation2(target + "++", target + " has a karma level of 1, " + SENDER, "");
        String message = SENDER + " added 'karma " + target + "' with " + "a value of '1'";
        log.debug("looking for " + message);
        Assert.assertTrue(changesDao.findLog(message));
        forgetFactoid2("karma " + target);
        log.debug("END ADDLOG");
    }

    public void logChanged() {
        log.debug("BEGIN LOGCHANGED");
        String target = "javabot";
        int karma = getKarma(target) + 1;
        testOperation2(target + "++", target + " has a karma level of " + karma + ", " + SENDER, "");
        String message = SENDER + " changed 'karma " + target + "' to" + " '" + karma + "'";
        log.debug("looking for " + message);
        Assert.assertTrue(changesDao.findLog(message));
        log.debug("END LOGCHANGED");
    }

    public void changeOwnKarma() {
        int karma = getKarma(SENDER);
        testOperation2(SENDER + "++", new String[]{"Changing one's own karma is not permitted.", "You now have a karma level of " + (karma - 1)}, "");
        int karma2 = getKarma(SENDER);
        Assert.assertTrue(karma2 == karma - 1, "Should have lost one karma point.");
    }

    private int getKarma(String target) {
        log.debug("target = " + target);
        Factoid factoid = factoidDao.getFactoid("karma " + target);
        String value = factoid.getValue();
        log.debug("value = " + value);

        return Integer.parseInt(value);
    }

    protected BotOperation getOperation2() {

        KarmaChangeOperation2 operation = null;
        try {
            operation = new KarmaChangeOperation2(factoidDao, changesDao, "test", new Javabot());
            return operation;
        } catch (Exception e) {
            Assert.fail("Could not create operation");
        }

        return operation;
    }
}