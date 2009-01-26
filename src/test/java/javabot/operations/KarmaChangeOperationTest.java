package javabot.operations;

import java.util.Date;

import javabot.dao.ChangeDao;
import javabot.dao.KarmaDao;
import javabot.model.Karma;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"operations"})
public class KarmaChangeOperationTest extends BaseOperationTest {
    @Autowired
    private KarmaDao karmaDao;
    @Autowired
    private ChangeDao changeDao;

    public void updateKarma() {
        final Karma karma = karmaDao.find("testjavabot");
        final int value = karma != null ? karma.getValue() : 0;
        testOperation("testjavabot++", "testjavabot has a karma level of " + (value+1) + ", " + SENDER);
        testOperation("testjavabot++", "Rest those fingers, Tex");
        testOperation("testjavabot--", "Rest those fingers, Tex");
        testOperation("testjavabot--", "Rest those fingers, Tex");
        testOperation("testjavabot--", "Rest those fingers, Tex");
        testOperation("testjavabot++", "Rest those fingers, Tex");
    }

    public void logNew() {
        final String target = new Date().getTime() + "";
        final int karma = getKarma(target) + 1;
        testOperation(target + "++", target + " has a karma level of " + karma + ", " + SENDER);
        final String message = SENDER + " changed '" + target + "' to '" + karma + "'";
        Assert.assertTrue(changeDao.findLog(message));
        karmaDao.delete(karmaDao.find(target).getId());
    }

    public void logChanged() {
        final String target = "javabot";
        final int karma = getKarma(target) + 1;
        testOperation(target + "++", target + " has a karma level of " + karma + ", " + SENDER);
    }

    public void changeOwnKarma() {
        final int karma = getKarma(SENDER);
        testOperation(SENDER + "++",
            new String[]{"Changing one's own karma is not permitted.", "You now have a karma level of " + (karma - 1)},
            "");
        final int karma2 = getKarma(SENDER);
        Assert.assertTrue(karma2 == karma - 1, "Should have lost one karma point.");
    }

    private int getKarma(final String target) {
        final Karma karma = karmaDao.find(target);
        return karma != null ? karma.getValue() : 0;
    }

    @Override
    protected BotOperation createOperation() {
        return new KarmaChangeOperation(getJavabot());
    }
}
