package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.dao.FactoidDao;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;

@Test
public class GetFactoidOperationTest extends BaseOperationTest {
    private static final String REPLY_VALUE = "I'm a reply!";
    @Inject
    private FactoidDao factoidDao;

    @BeforeClass
    public void createGets() {
        deleteFactoids();
        factoidDao.addFactoid(TEST_NICK, "api", "http://java.sun.com/javase/current/docs/api/index.html");
        factoidDao.addFactoid(TEST_NICK, "replyTest", "<reply>I'm a reply!");
        factoidDao.addFactoid(TEST_NICK, "stupid", "<reply>$who, what you've just said is one of the most insanely idiotic things I" +
                                                   " have ever heard. At no point in your rambling, incoherent response were you even" +
                                                   " close to anything that could be considered a rational thought. Everyone in this" +
                                                   " room is now dumber for having listened to it. I award you no points, and may God" +
                                                   " have mercy on your soul.");
        factoidDao.addFactoid(TEST_NICK, "seeTest", "<see>replyTest");
        factoidDao.addFactoid(TEST_NICK, "noReply", "I'm a reply!");
        factoidDao.addFactoid(TEST_NICK, "replace $1", "<reply>I replaced you $1");
        factoidDao.addFactoid(TEST_NICK, "camel $^", "<reply>$^");
        factoidDao.addFactoid(TEST_NICK, "url $+", "<reply>$+");
        factoidDao.addFactoid(TEST_NICK, "hey", "<reply>Hello, $who");
        factoidDao.addFactoid(TEST_NICK, "coin", "<reply>(heads|tails)");
        factoidDao.addFactoid(TEST_NICK, "hug $1", "<action> hugs $1");
    }

    @AfterClass
    public void deleteFactoids() {
        delete("api");
        delete("stupid");
        delete("replyTest");
        delete("seeTest");
        delete("noReply");
        delete("replace $1");
        delete("url $+");
        delete("camel $C");
        delete("camel $^");
        delete("camel $+");
        delete("hey");
        delete("coin");
        delete("hug $1");
    }

    private void delete(final String key) {
        while (factoidDao.hasFactoid(key)) {
            factoidDao.delete(TEST_NICK, key);
        }
    }

    public void straightGets() throws IOException {
        testMessage("~api", getFoundMessage("api", "http://java.sun.com/javase/current/docs/api/index.html"));
        Assert.assertNotNull(factoidDao.getFactoid("api").getLastUsed());
    }

    public void replyGets() {
        testMessage("~replyTest", REPLY_VALUE);
    }

    public void seeGets() {
        testMessage("~seeTest", REPLY_VALUE);
    }

    public void seeReplyGets() {
        testMessage("~seeTest", REPLY_VALUE);
    }

    public void parameterReplacement() {
        testMessage("~replace " + getTestUser(), "I replaced you " + getTestUser());
        testMessage("~url what up doc", "what+up+doc");
        testMessage("~camel i should be camel case", "IShouldBeCamelCase");
    }

    public void whoReplacement() {
        testMessage("~hey", "Hello, " + getTestUser());
    }

    public void randomList() {
        testMessageList("~coin", Arrays.asList("heads", "tails"));
    }

    @Test(enabled = false)
    public void guessFactoid() {
        testMessage("~bre", "I guess the factoid 'label line breaks' might be appropriate:");
    }

    public void noGuess() {
        testMessage("~apiz", Sofia.unhandledMessage(getTestUser().getNick()));
    }

    public void action() {
        testMessage("~hug " + TEST_NICK, " hugs " + TEST_NICK);
    }

    @Test
    public void tell() {
        testMessage(String.format("~tell %s about hey", TEST_NICK), "Hello, " + TEST_NICK);
        testMessage(String.format("~tell %s about camel I am a test", TEST_NICK), TEST_NICK + ", IAmATest");
        testMessage(String.format("~tell %s about url I am a test", TEST_NICK), String.format("%s, I+am+a+test", TEST_NICK));
        testMessage(String.format("~tell %s about stupid", TEST_NICK),
                    String.format("%s, what you've just said is one of the most"
                                  + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
                                  + " even close to anything that could be considered a rational thought. Everyone in this room is now"
                                  + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.",
                                  TEST_NICK));
        sleep(6000);
        testMessage(String.format("~~ %s seeTest", TEST_NICK), String.format("%s, I'm a reply!", TEST_NICK));
        testMessage(String.format("~~ %s bobloblaw", TEST_NICK), Sofia.unhandledMessage(getTestUser().getNick()));
        testMessage(String.format("~~ %s api", TEST_NICK),
                    String.format("%s, api is http://java.sun.com/javase/current/docs/api/index.html", TEST_NICK));
        validate("camel I am a test 2", "IAmATest2");
        testMessage(String.format("~~ %s url I am a test 2", TEST_NICK), String.format("%s, I+am+a+test+2", TEST_NICK));
        //    scanForResponse(String.format("~~ %s javadoc String", TEST_NICK), "[JDK: java.lang.String]");
        //    scanForResponse(String.format("~~ %s javadoc String", new IrcUser("jimbob")), "jimbob");
        testMessage(String.format("~~ %s stupid", TEST_NICK),
                    String.format("%s, what you've just said is one of the most"
                                  + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
                                  + " even close to anything that could be considered a rational thought. Everyone in this room is now"
                                  + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.",
                                  TEST_NICK));

        testMessage(String.format("~~%s seeTest", TEST_NICK), String.format("%s, I'm a reply!", TEST_NICK));
        testMessage(String.format("~~%s bobloblaw", TEST_NICK), Sofia.unhandledMessage(getTestUser().getNick()));

        testMessage(String.format("~~%s api", TEST_NICK),
                    String.format("%s, api is http://java.sun.com/javase/current/docs/api/index.html", TEST_NICK));
        testMessage(String.format("~~%s camel I am a test 3", TEST_NICK), String.format("%s, IAmATest3", TEST_NICK));
        testMessage(String.format("~~%s url I am a test 3", TEST_NICK), String.format("%s, I+am+a+test+3", TEST_NICK));
        validate("stupid", "what you've just said is one of the most insanely idiotic things I have ever heard. At no" +
                           " point in your rambling, incoherent response were you even close to anything that could be considered" +
                           " a rational thought. Everyone in this room is now dumber for having listened to it. I award you no" +
                           " points, and may God have mercy on your soul.");
    }

    private void validate(final String factoid, final String response) {
        testMessage(String.format("~~ %s " + factoid, TEST_NICK), String.format("%s, " + response, TEST_NICK));
    }
}