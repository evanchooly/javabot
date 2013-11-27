package javabot.operations;

import javabot.BaseTest;
import javabot.IrcUser;
import javabot.dao.ChangeDao;
import javabot.dao.KarmaDao;
import javabot.model.Karma;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.Date;

@Test(groups = {"operations"})
public class KarmaOperationTest extends BaseOperationTest {
    @Inject
    private KarmaDao karmaDao;
    @Inject
    private ChangeDao changeDao;

    public void updateKarma() throws InterruptedException {
        final Karma karma = karmaDao.find("testjavabot");
        int value = karma != null ? karma.getValue() : 0;
        testMessage("~testjavabot++",
                String.format("testjavabot has a karma level of %d, %s", ++value, BaseTest.TEST_USER));
        testMessage("~testjavabot++", "Rest those fingers, Tex");
        testMessage("~testjavabot--", "Rest those fingers, Tex");
        testMessage("~testjavabot--", "Rest those fingers, Tex");
        testMessage("~testjavabot--", "Rest those fingers, Tex");
        System.out.println("Sleeping 7s...");
        Thread.sleep(7000);
        testMessage("~testjavabot++",
                String.format("testjavabot has a karma level of %d, %s", ++value, BaseTest.TEST_USER));
        System.out.println("Sleeping 7s...");
        Thread.sleep(7000);
        testMessage("~testjavabot--",
                String.format("testjavabot has a karma level of %d, %s", --value, BaseTest.TEST_USER));
        System.out.println("Sleeping 7s...");
        Thread.sleep(7000);
        testMessage("~testjavabot--", String.format("testjavabot has a karma level of %d, %s", --value,
                BaseTest.TEST_USER));
    }

    public void noncontiguousNameReadKarma() {
        final String target = "foo " + new Date().getTime();
        final int karma = getKarma(new IrcUser(target, target, "localhost"));
        testMessage("~karma " + target, target + " has no karma, " + BaseTest.TEST_USER);
    }

    public void noncontiguousNameAddKarma() {
        final String target = "foo " + new Date().getTime();
        final int karma = getKarma(new IrcUser(target, target, "localhost")) + 1;
        testMessage("~" + target + "++", target + " has a karma level of " + karma + ", " + BaseTest.TEST_USER);
        final String message = BaseTest.TEST_USER + " changed '" + target + "' to '" + karma + "'";
        Assert.assertTrue(changeDao.findLog(message));
        karmaDao.delete(karmaDao.find(target).getId());
    }

    public void noncontiguousNameAddKarmaWithComment() {
        final String target = "foo " + new Date().getTime();
        final int karma = getKarma(new IrcUser(target, target, "localhost")) + 1;
        testMessage("~" + target + "++ hey coolio", target + " has a karma level of " + karma + ", " + BaseTest.TEST_USER);
        final String message = BaseTest.TEST_USER + " changed '" + target + "' to '" + karma + "'";
        Assert.assertTrue(changeDao.findLog(message));
        karmaDao.delete(karmaDao.find(target).getId());
    }

    public void logNew() {
        final String target = new Date().getTime() + "";
        final int karma = getKarma(new IrcUser(target, target, "localhost")) + 1;
        testMessage("~" + target + "++", target + " has a karma level of " + karma + ", " + BaseTest.TEST_USER);
        final String message = BaseTest.TEST_USER + " changed '" + target + "' to '" + karma + "'";
        Assert.assertTrue(changeDao.findLog(message));
        karmaDao.delete(karmaDao.find(target).getId());
    }

    public void logChanged() {
        final String target = "javabot";
        final int karma = getKarma(new IrcUser(target, target, "localhost")) + 1;
        testMessage("~" + target + "++", target + " has a karma level of " + karma + ", " + BaseTest.TEST_USER);
    }

    public void changeOwnKarma() {
        final int karma = getKarma(BaseTest.TEST_USER);
        sendMessage("~" + BaseTest.TEST_USER + "++");
        final int karma2 = getKarma(BaseTest.TEST_USER);
        Assert.assertTrue(karma2 == karma - 1, "Should have lost one karma point.");
    }

    private int getKarma(final IrcUser target) {
        final Karma karma = karmaDao.find(target.getNick());
        return karma != null ? karma.getValue() : 0;
    }
}
