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

    public void updateKarma() throws InterruptedException {
        final Karma karma = karmaDao.find("testjavabot");
        int value = karma != null ? karma.getValue() : 0;
        testMessage("testjavabot++",
            String.format("testjavabot has a karma level of %d, %s", ++value, getTestBot().getNick()));
        testMessage("testjavabot++", "Rest those fingers, Tex");
        testMessage("testjavabot--", "Rest those fingers, Tex");
        testMessage("testjavabot--", "Rest those fingers, Tex");
        testMessage("testjavabot--", "Rest those fingers, Tex");
        Thread.sleep(21000);
        testMessage("testjavabot++",
            String.format("testjavabot has a karma level of %d, %s", ++value, getTestBot().getNick()));
        Thread.sleep(21000);
        testMessage("testjavabot--",
            String.format("testjavabot has a karma level of %d, %s", --value, getTestBot().getNick()));
    }

    public void logNew() {
        final String target = new Date().getTime() + "";
        final int karma = getKarma(target) + 1;
        testMessage(target + "++", target + " has a karma level of " + karma + ", " + getTestBot().getNick());
        final String message = getTestBot().getNick() + " changed '" + target + "' to '" + karma + "'";
        Assert.assertTrue(changeDao.findLog(message));
        karmaDao.delete(karmaDao.find(target).getId());
    }

    public void logChanged() {
        final String target = "javabot";
        final int karma = getKarma(target) + 1;
        testMessage(target + "++", target + " has a karma level of " + karma + ", " + getTestBot().getNick());
    }

    public void changeOwnKarma() {
        final int karma = getKarma(getTestBot().getNick());
        testMessage(getTestBot().getNick() + "++",
            "Changing one's own karma is not permitted.", getTestBot().getNick() + ", you have a karma level of " + (karma - 1));
        final int karma2 = getKarma(getTestBot().getNick());
        Assert.assertTrue(karma2 == karma - 1, "Should have lost one karma point.");
    }

    private int getKarma(final String target) {
        final Karma karma = karmaDao.find(target);
        return karma != null ? karma.getValue() : 0;
    }
}
