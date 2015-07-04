package javabot.operations;

import com.antwerkz.sofia.Sofia;
import com.jayway.awaitility.Awaitility;
import javabot.BaseMessagingTest;
import javabot.BotListener;
import javabot.Message;
import javabot.Messages;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import javabot.dao.KarmaDao;
import javabot.model.Karma;
import javabot.model.UserFactory;
import org.pircbotx.User;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Test(groups = {"operations"})
public class AddFactoidOperationTest extends BaseMessagingTest {
    @Inject
    private FactoidDao factoidDao;
    @Inject
    private ChangeDao changeDao;
    @Inject
    private BotListener listener;
    @Inject
    private KarmaDao karmaDao;
    @Inject
    private UserFactory userFactory;

    @BeforeMethod
    public void setUp() {
        factoidDao.delete(TEST_NICK, "test");
        factoidDao.delete(TEST_NICK, "ping $1");
        factoidDao.delete(TEST_NICK, "what");
        factoidDao.delete(TEST_NICK, "what up");
        factoidDao.delete(TEST_NICK, "test pong");
        factoidDao.delete(TEST_NICK, "asdf");
        factoidDao.delete(TEST_NICK, "12345");
        factoidDao.delete(TEST_NICK, "replace");
    }

    public void factoidAdd() {
        testMessage("~test pong is pong", ok);
        testMessage("~ping $1 is <action>sends some radar to $1, awaits a response then forgets how long it took",
                    ok);
        testMessage("~what? is a question", ok);
        testMessage("~what up? is <see>what?", ok);
    }

    public void replace() {
        testMessage("~replace is first entry", ok);
        testMessage("~no, replace is <reply>second entry", Sofia.ok(getTestUser().getNick()));
        testMessage("~replace", "second entry");
        forgetFactoid("replace");
        testMessage("~no, replace is <reply>second entry", Sofia.ok(getTestUser().getNick()));
    }

    /** 
     * This test relies on the KarmaOperation being in the operation set.
     */
    @Test    
    public void testAddOperationPriority() {
        final String target = "foo is " + new Date().getTime();
        final int karma = getKarma(userFactory.createUser(target, target, "localhost")) + 1;
        testMessage("~" + target + "++", Sofia.karmaOthersValue(target, karma, getTestUser().getNick()));
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(getTestUser().getNick(), target, karma)));
        karmaDao.delete(karmaDao.find(target).getId());    }

    @Test(dependsOnMethods = {"factoidAdd"})
    public void duplicateAdd() throws IOException {
        final String message = "~test pong is pong";
        testMessage(message, ok);
        testMessage(message, Sofia.factoidExists("test pong", getTestUser().getNick()));
        forgetFactoid("test pong");
    }

    public void blankValue() {
        testMessage("~pong is", Sofia.unhandledMessage(getTestUser().getNick()));
    }

    public void addLog() {
        testMessage("~12345 is 12345", ok);
        Assert.assertTrue(changeDao.findLog(Sofia.factoidAdded(getTestUser().getNick(), "12345", "12345")));
        forgetFactoid("12345");
    }

    public void parensFactoids() {
        final String factoid = "should be the full (/hi there) factoid";
        testMessage("~asdf is <reply>" + factoid, ok);
        testMessage("~asdf", factoid);
    }

    public void privMessage() {
        listener.onPrivateMessage(new PrivateMessageEvent<>(getIrcBot(), getTestUser(), "privMessage is doh!"));
        Awaitility.await()
            .atMost(10, TimeUnit.SECONDS)
            .until(() -> !getMessages().isEmpty());
        Messages messages = getMessages();
        Assert.assertFalse(messages.isEmpty());
    }

    private int getKarma(final User target) {
        final Karma karma = karmaDao.find(target.getNick());
        return karma != null ? karma.getValue() : 0;
    }
}