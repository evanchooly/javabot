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
        Karma karma = karmaDao.find("testjavabot");
        int value = karma != null ? karma.getValue() : 0;
        testOperation("testjavabot++", "testjavabot has a karma level of " + (value+1) + ", " + SENDER);
        testOperation("testjavabot++", "Rest those fingers, Tex");
        testOperation("testjavabot--", "Rest those fingers, Tex");
        testOperation("testjavabot--", "Rest those fingers, Tex");
        testOperation("testjavabot--", "Rest those fingers, Tex");
        testOperation("testjavabot++", "Rest those fingers, Tex");
    }

    public void logNew() {
        String target = new Date().getTime() + "";
        int karma = getKarma(target) + 1;
        testOperation(target + "++", target + " has a karma level of " + karma + ", " + SENDER);
        String message = SENDER + " changed '" + target + "' to '" + karma + "'";
        Assert.assertTrue(changeDao.findLog(message));
        karmaDao.delete(karmaDao.find(target).getId());
    }

    public void logChanged() {
        String target = "javabot";
        int karma = getKarma(target) + 1;
        testOperation(target + "++", target + " has a karma level of " + karma + ", " + SENDER);
    }

    public void changeOwnKarma() {
        int karma = getKarma(SENDER);
        testOperation(SENDER + "++",
            new String[]{"Changing one's own karma is not permitted.", "You now have a karma level of " + (karma - 1)},
            "");
        int karma2 = getKarma(SENDER);
        Assert.assertTrue(karma2 == karma - 1, "Should have lost one karma point.");
    }

    private int getKarma(String target) {
        Karma karma = karmaDao.find(target);
        return karma != null ? karma.getValue() : 0;
    }

    @Override
    protected BotOperation createOperation() {
        return new KarmaChangeOperation(getJavabot());
    }
}
