package javabot.operations;

import java.util.Date;
import javax.inject.Inject;

import com.antwerkz.sofia.Sofia;
import javabot.BaseTest;
import javabot.dao.ChangeDao;
import javabot.dao.ConfigDao;
import javabot.dao.KarmaDao;
import javabot.dao.NickServDao;
import javabot.model.Config;
import javabot.model.IrcUser;
import javabot.model.Karma;
import javabot.model.NickServInfo;
import javabot.model.ThrottleItem;
import org.mongodb.morphia.Datastore;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"operations"})
public class KarmaOperationTest extends BaseOperationTest {
  @Inject
  private KarmaDao karmaDao;

  @Inject
  private ChangeDao changeDao;

  @Inject
  private NickServDao nickServDao;

  @Inject
  private Datastore ds;

  @Inject
  private ConfigDao configDao;

  public void updateKarma() throws InterruptedException {
    Config config = configDao.get();
    Integer throttleThreshold = config.getThrottleThreshold();
    try {
      ds.delete(ds.createQuery(ThrottleItem.class));
      final Karma karma = karmaDao.find("testjavabot");
      int value = karma != null ? karma.getValue() : 0;
      final IrcUser bob = new IrcUser("bob", "bob", "localhost");
      NickServInfo info = new NickServInfo(bob);
      info.setRegistered(info.getRegistered().minusDays(100));
      nickServDao.clear();
      nickServDao.save(info);
      for (int i = 0; i < throttleThreshold; i++) {
        testMessageAs(bob, "~testjavabot++",
            String.format("testjavabot has a karma level of %d, %s", ++value, bob.getNick()));
      }
      testMessageAs(bob, "~testjavabot++", Sofia.throttledUser());
      testMessageAs(bob, "~testjavabot--", Sofia.throttledUser());
      testMessageAs(bob, "~testjavabot--", Sofia.throttledUser());
      testMessageAs(bob, "~testjavabot--", Sofia.throttledUser());

      ds.delete(ds.createQuery(ThrottleItem.class));

      testMessageAs(bob, "~testjavabot++",
          String.format("testjavabot has a karma level of %d, %s", ++value, bob.getNick()));
    } finally {
      config.setThrottleThreshold(throttleThreshold);
      configDao.save(config);
    }
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

  public void karmaLooksLikeParam() {
    final String target = "foo " + new Date().getTime();
    final int karma = getKarma(new IrcUser(target, target, "localhost")) - 1;
    testMessage("~" + target + "--bar=as", target + " has a karma level of " + karma + ", " + BaseTest.TEST_USER);
    final String message = BaseTest.TEST_USER + " changed '" + target + "' to '" + karma + "'";
    Assert.assertTrue(changeDao.findLog(message));
    karmaDao.delete(karmaDao.find(target).getId());
  }

  public void karmaLooksLikeParamShort() {
    testMessage("~--bar=as", BaseTest.TEST_USER + ", what does that even *mean*?");
    testMessage("~ --bar=af", BaseTest.TEST_USER + ", what does that even *mean*?");
  }

  public void noncontiguousNameAddKarmaTrailingSpace() {
    final String target = "foo " + new Date().getTime();
    final int karma = getKarma(new IrcUser(target, target, "localhost")) + 1;
    testMessage("~" + target + " ++", target + " has a karma level of " + karma + ", " + BaseTest.TEST_USER);
    final String message = BaseTest.TEST_USER + " changed '" + target + "' to '" + karma + "'";
    Assert.assertTrue(changeDao.findLog(message));
    karmaDao.delete(karmaDao.find(target).getId());
  }

  @Test
  public void noncontiguousNameAddKarmaWithComment() {
    final String target = "foo " + new Date().getTime();
    final int karma = getKarma(new IrcUser(target, target, "localhost")) + 1;
    testMessage("~" + target + "++ hey coolio", target + " has a karma level of " + karma + ", " + BaseTest.TEST_USER);
    final String message = BaseTest.TEST_USER + " changed '" + target + "' to '" + karma + "'";
    Assert.assertTrue(changeDao.findLog(message));
    karmaDao.delete(karmaDao.find(target).getId());
  }

  public void shortNameAddKarma() {
    final String target = "a"; // shortest possible name
    final int karma = getKarma(new IrcUser(target, target, "localhost")) + 1;
    testMessage("~" + target + "++", target + " has a karma level of " + karma + ", " + BaseTest.TEST_USER);
    final String message = BaseTest.TEST_USER + " changed '" + target + "' to '" + karma + "'";
    Assert.assertTrue(changeDao.findLog(message));
    karmaDao.delete(karmaDao.find(target).getId());
  }

  public void noNameAddKarma() {
    final String target = ""; // no name
    final int karma = getKarma(new IrcUser(target, target, "localhost")) + 1;
    testMessage("~" + target + "++", BaseTest.TEST_USER + ", what does that even *mean*?");
    final String message = BaseTest.TEST_USER + " changed '" + target + "' to '" + karma + "'";
    Assert.assertFalse(changeDao.findLog(message));
  }

  public void noNameSubKarma() {
    final String target = ""; // no name
    final int karma = getKarma(new IrcUser(target, target, "localhost")) - 1;
    testMessage("~" + target + "--", BaseTest.TEST_USER + ", what does that even *mean*?");
    final String message = BaseTest.TEST_USER + " changed '" + target + "' to '" + karma + "'";
    Assert.assertFalse(changeDao.findLog(message));
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
