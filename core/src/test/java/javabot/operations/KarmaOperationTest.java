package javabot.operations;

import java.util.Date;
import javax.inject.Inject;

import javabot.BaseTest;
import javabot.IrcUser;
import javabot.dao.ChangeDao;
import javabot.dao.KarmaDao;
import javabot.model.Karma;
import org.testng.Assert;
import org.testng.annotations.Test;

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
    Thread.sleep(7000);
    testMessage("~testjavabot++",
        String.format("testjavabot has a karma level of %d, %s", ++value, BaseTest.TEST_USER));
    Thread.sleep(7000);
    testMessage("~testjavabot--",
        String.format("testjavabot has a karma level of %d, %s", --value, BaseTest.TEST_USER));
    Thread.sleep(7000);
    testMessage("~testjavabot--", String.format("testjavabot has a karma level of %d, %s", --value,
        BaseTest.TEST_USER));
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
    testMessage("~" + BaseTest.TEST_USER + "++",
        "Changing one's own karma is not permitted.",
        BaseTest.TEST_USER + ", you have a karma level of " + (karma - 1));
    final int karma2 = getKarma(BaseTest.TEST_USER);
    Assert.assertTrue(karma2 == karma - 1, "Should have lost one karma point.");
  }

  private int getKarma(final IrcUser target) {
    final Karma karma = karmaDao.find(target.getNick());
    return karma != null ? karma.getValue() : 0;
  }
}
