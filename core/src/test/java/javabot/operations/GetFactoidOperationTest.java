package javabot.operations;

import java.io.IOException;
import java.util.Arrays;

import javabot.BaseTest;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class GetFactoidOperationTest extends BaseOperationTest {
    private static final String REPLY_VALUE = "I'm a reply!";
    @Autowired
    private FactoidDao factoidDao;

    @BeforeClass
    public void createGets() {
        deleteFactoids();
        factoidDao.addFactoid(BaseTest.TEST_USER, "api", "http://java.sun.com/javase/current/docs/api/index.html");
        factoidDao.addFactoid(BaseTest.TEST_USER, "replyTest", "<reply>I'm a reply!");
        factoidDao.addFactoid(BaseTest.TEST_USER, "stupid", "<reply>$who, what you've just said is one of the most"
            + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
            + " even close to anything that could be considered a rational thought. Everyone in this room is now"
            + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.");
        factoidDao.addFactoid(BaseTest.TEST_USER, "seeTest", "<see>replyTest");
        factoidDao.addFactoid(BaseTest.TEST_USER, "noReply", "I'm a reply!");
        factoidDao.addFactoid(BaseTest.TEST_USER, "replace $1", "<reply>I replaced you $1");
        factoidDao.addFactoid(BaseTest.TEST_USER, "camel $^", "<reply>$^");
        factoidDao.addFactoid(BaseTest.TEST_USER, "url $+", "<reply>$+");
        factoidDao.addFactoid(BaseTest.TEST_USER, "hey", "<reply>Hello, $who");
        factoidDao.addFactoid(BaseTest.TEST_USER, "coin", "<reply>(heads|tails)");
        factoidDao.addFactoid(BaseTest.TEST_USER, "hug $1", "<action> hugs $1");
    }

    @AfterClass
    public void deleteFactoids() {
        delete("api");
        delete("replyTest");
        delete("seeTest");
        delete("noReply");
        delete("replace $1");
        delete("url $+");
        delete("camel $C");
        delete("hey");
        delete("coin");
        delete("hug $1");
    }

    private void delete(final String key) {
        while (factoidDao.hasFactoid(key)) {
            factoidDao.delete(BaseTest.TEST_USER, key);
        }
    }

    public void straightGets() throws IOException {
        testMessage("~api", getFoundMessage("api", "http://java.sun.com/javase/current/docs/api/index.html"));
        Assert.assertNotNull(factoidDao.getFactoid("api").getLastUsed());
    }

    public void embeddedGets() {
        testMessage("mumble curse (~api)", getFoundMessage("api", "http://java.sun.com/javase/current/docs/api/index.html"));
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
        testMessage("~replace " + BaseTest.TEST_USER, "I replaced you " + BaseTest.TEST_USER);
        testMessage("~url what up doc", "what+up+doc");
        testMessage("~camel i should be camel case", "IShouldBeCamelCase");
    }

    public void whoReplacement() {
        testMessage("~hey", "Hello, " + BaseTest.TEST_USER);
    }

    public void randomList() {
        testMessageList("~coin", Arrays.asList("heads", "tails"));
    }

    @Test(enabled = false)
    public void guessFactoid() {
        testMessage("~bre", "I guess the factoid 'label line breaks' might be appropriate:");
    }

    public void noGuess() {
        testMessage("~apiz", BaseTest.TEST_USER + ", I have no idea what apiz is.");
    }

    public void action() {
        testMessage("~hug " + BaseTest.TEST_USER, " hugs " + BaseTest.TEST_USER);
    }

    @Test
    public void tell() {
        final String nick = TEST_USER;
        testMessage(String.format("~tell %s about hey", nick), "Hello, " + nick);
        testMessage(String.format("~tell %s about camel I am a test", nick), nick + ", IAmATest");
        testMessage(String.format("~tell %s about url I am a test", nick), String.format("%s, I+am+a+test", nick));
        testMessage(String.format("~tell %s about javadoc String", nick), String.format("%s: http://is.gd/ekPI3 [JDK: java.lang.String]", nick));
        testMessage(String.format("~tell %s about stupid", nick), String.format("%s, what you've just said is one of the most"
            + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
            + " even close to anything that could be considered a rational thought. Everyone in this room is now"
            + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.", nick));

        sleep(6000);
        testMessage(String.format("~~ %s seeTest", nick), String.format("%s, I'm a reply!", nick));

        testMessage(String.format("~~ %s bobloblaw", nick),
            String.format("%s, I have no idea what bobloblaw is.", nick));

        testMessage(String.format("~~ %s api", nick),
            String.format("%s, api is http://java.sun.com/javase/current/docs/api/index.html", nick));

        testMessage(String.format("~~ %s camel I am a test 2", nick), String.format("%s, IAmATest2", nick));

        testMessage(String.format("~~ %s url I am a test 2", nick), String.format("%s, I+am+a+test+2", nick));

        scanForResponse(String.format("~~ %s javadoc String", nick), "[JDK: java.lang.String]");

        testMessage(String.format("~~ %s stupid", nick), String.format("%s, what you've just said is one of the most"
            + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
            + " even close to anything that could be considered a rational thought. Everyone in this room is now"
            + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.", nick));

        sleep(6000);
        testMessage(String.format("~~%s seeTest", nick), String.format("%s, I'm a reply!", nick));

        testMessage(String.format("~~%s bobloblaw", nick), String.format("%s, I have no idea what bobloblaw is.", nick));

        testMessage(String.format("~~%s api", nick),
            String.format("%s, api is http://java.sun.com/javase/current/docs/api/index.html", nick));

        testMessage(String.format("~~%s camel I am a test 3", nick), String.format("%s, IAmATest3", nick));

        testMessage(String.format("~~%s url I am a test 3", nick), String.format("%s, I+am+a+test+3", nick));

        scanForResponse(String.format("~~%s javadoc String", nick), "[JDK: java.lang.String]");

        testMessage(String.format("~~%s stupid", nick), String.format("%s, what you've just said is one of the most"
            + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
            + " even close to anything that could be considered a rational thought. Everyone in this room is now"
            + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.", nick));
    }
}