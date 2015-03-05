package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.BaseMessagingTest;
import javabot.dao.ChangeDao;
import javabot.dao.ConfigDao;
import javabot.dao.KarmaDao;
import javabot.model.Config;
import javabot.model.Karma;
import javabot.model.ThrottleItem;
import javabot.model.UserFactory;
import org.mongodb.morphia.Datastore;
import org.pircbotx.User;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.Date;

@Test(groups = {"operations"})
public class KarmaOperationTest extends BaseMessagingTest {
    @Inject
    private UserFactory userFactory;

    @Inject
    private KarmaDao karmaDao;

    @Inject
    private ChangeDao changeDao;

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
            final User bob = registerIrcUser("bob", "bob", "localhost");
            for (int i = 0; i < throttleThreshold; i++) {
                testMessageAs(bob, "~testjavabot++", Sofia.karmaOthersValue("testjavabot", ++value, bob.getNick()));
            }
            testMessageAs(bob, "~testjavabot++", Sofia.throttledUser());
            testMessageAs(bob, "~testjavabot--", Sofia.throttledUser());
            testMessageAs(bob, "~testjavabot--", Sofia.throttledUser());
            testMessageAs(bob, "~testjavabot--", Sofia.throttledUser());

            ds.delete(ds.createQuery(ThrottleItem.class));

            testMessageAs(bob, "~testjavabot++", Sofia.karmaOthersValue("testjavabot", ++value, bob.getNick()));
        } finally {
            config.setThrottleThreshold(throttleThreshold);
            configDao.save(config);
        }
    }

    public void noncontiguousNameReadKarma() {
        final String target = "foo " + new Date().getTime();
        testMessage("~karma " + target, target + " has no karma, " + getTestUser());
    }

    public void noncontiguousNameAddKarma() {
        final String target = "foo " + new Date().getTime();
        final int karma = getKarma(userFactory.createUser(target, target, "localhost")) + 1;
        testMessage("~" + target + "++", Sofia.karmaOthersValue(target, karma, getTestUser().getNick()));
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(getTestUser().getNick(), target, karma)));
        karmaDao.delete(karmaDao.find(target).getId());
    }

    public void karmaLooksLikeParam() {
        final String target = "foo " + new Date().getTime();
        final int karma = getKarma(userFactory.createUser(target, target, "localhost")) - 1;
        testMessage("~" + target + "--bar=as", Sofia.karmaOthersValue(target, karma, getTestUser().getNick()));
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(getTestUser().getNick(), target, karma)));
        karmaDao.delete(karmaDao.find(target).getId());
    }

    public void karmaLooksLikeParamShort() {
        testMessage("~--bar=as", Sofia.unhandledMessage(getTestUser().getNick()));
        testMessage("~ --bar=af", Sofia.unhandledMessage(getTestUser().getNick()));
    }

    public void noncontiguousNameAddKarmaTrailingSpace() {
        final String target = "foo " + new Date().getTime();
        final int karma = getKarma(userFactory.createUser(target, target, "localhost")) + 1;
        testMessage("~" + target + " ++", Sofia.karmaOthersValue(target, karma, getTestUser().getNick()));
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(getTestUser().getNick(), target, karma)));
        karmaDao.delete(karmaDao.find(target).getId());
    }

    @Test
    public void noncontiguousNameAddKarmaWithComment() {
        final String target = "foo " + new Date().getTime();
        final int karma = getKarma(userFactory.createUser(target, target, "localhost")) + 1;
        testMessage("~" + target + "++ hey coolio", Sofia.karmaOthersValue(target, karma, getTestUser().getNick()));
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(getTestUser().getNick(), target, karma)));
        karmaDao.delete(karmaDao.find(target).getId());
    }

    public void shortNameAddKarma() {
        final String target = "a"; // shortest possible name
        final int karma = getKarma(userFactory.createUser(target, target, "localhost")) + 1;
        testMessage("~" + target + "++", Sofia.karmaOthersValue(target, karma, getTestUser().getNick()));
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(getTestUser().getNick(), target, karma)));
        karmaDao.delete(karmaDao.find(target).getId());
    }

    public void noNameAddKarma() {
        final String target = ""; // no name
        final int karma = getKarma(userFactory.createUser(target, target, "localhost")) + 1;
        testMessage("~" + target + "++", Sofia.unhandledMessage(getTestUser().getNick()));
        Assert.assertFalse(changeDao.findLog(Sofia.karmaChanged(getTestUser().getNick(), target, karma)));
    }

    public void noNameSubKarma() {
        final String target = ""; // no name
        final int karma = getKarma(userFactory.createUser(target, target, "localhost")) - 1;
        testMessage("~" + target + "--", Sofia.unhandledMessage(getTestUser().getNick()));
        Assert.assertFalse(changeDao.findLog(Sofia.karmaChanged(getTestUser().getNick(), target, karma)));
    }

    public void logNew() {
        final String target = new Date().getTime() + "";
        final int karma = getKarma(userFactory.createUser(target, target, "localhost")) + 1;
        testMessage("~" + target + "++", Sofia.karmaOthersValue(target, karma, getTestUser().getNick()));
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(getTestUser().getNick(), target, karma)));
        karmaDao.delete(karmaDao.find(target).getId());
    }

    public void logChanged() {
        final String target = "javabot";
        final int karma = getKarma(userFactory.createUser(target, target, "localhost")) + 1;
        testMessage("~" + target + "++", Sofia.karmaOthersValue(target, karma, getTestUser().getNick()));
    }

    public void changeOwnKarma() {
        final int karma = getKarma(getTestUser());
        testMessage("~" + getTestUser() + "++",
                "You can't increment your own karma.",
                getTestUser()+", you have a karma level of "+(karma-1));
        final int karma2 = getKarma(getTestUser());
        Assert.assertTrue(karma2 == karma - 1, "Should have lost one karma point.");
    }

    public void queryOwnKarma() {
        final User bill = registerIrcUser("bill", "bill", "localhost");
        final int karma = getKarma(bill);
        Assert.assertEquals(karma, 0);
        testMessageAs(bill, "~karma bill", Sofia.karmaOwnNone("bill"));
    }

    private int getKarma(final User target) {
        final Karma karma = karmaDao.find(target.getNick());
        return karma != null ? karma.getValue() : 0;
    }
}
