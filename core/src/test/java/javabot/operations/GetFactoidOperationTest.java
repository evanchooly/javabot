package javabot.operations;

import java.io.IOException;
import java.lang.String;
import java.util.Arrays;

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
        factoidDao.addFactoid(getTestBot().getNick(), "api", "http://java.sun.com/javase/current/docs/api/index.html");
        factoidDao.addFactoid(getTestBot().getNick(), "replyTest", "<reply>I'm a reply!");
        factoidDao.addFactoid(getTestBot().getNick(), "stupid", "<reply>$who, what you've just said is one of the most" 
            + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
            + " even close to anything that could be considered a rational thought. Everyone in this room is now"
            + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.");
        factoidDao.addFactoid(getTestBot().getNick(), "seeTest", "<see>replyTest");
        factoidDao.addFactoid(getTestBot().getNick(), "noReply", "I'm a reply!");
        factoidDao.addFactoid(getTestBot().getNick(), "replace $1", "<reply>I replaced you $1");
        factoidDao.addFactoid(getTestBot().getNick(), "camel $^", "<reply>$^");
        factoidDao.addFactoid(getTestBot().getNick(), "url $+", "<reply>$+");
        factoidDao.addFactoid(getTestBot().getNick(), "hey", "<reply>Hello, $who");
        factoidDao.addFactoid(getTestBot().getNick(), "coin", "<reply>(heads|tails)");
        factoidDao.addFactoid(getTestBot().getNick(), "hug $1", "<action> hugs $1");
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
            factoidDao.delete(getTestBot().getNick(), key);
        }
    }

    public void straightGets() throws IOException {
        testMessage("api", getFoundMessage("api", "http://java.sun.com/javase/current/docs/api/index.html"));
        Assert.assertNotNull(factoidDao.getFactoid("api").getLastUsed());
    }

    public void replyGets() {
        testMessage("replyTest", REPLY_VALUE);
    }

    public void seeGets() {
        testMessage("seeTest", REPLY_VALUE);
    }

    public void seeReplyGets() {
        testMessage("seeTest", REPLY_VALUE);
    }

    public void parameterReplacement() {
        testMessage("replace " + getTestBot().getNick(), "I replaced you " + getTestBot().getNick());
        testMessage("url what up doc", "what+up+doc");
        testMessage("camel i should be camel case", "IShouldBeCamelCase");
    }

    public void whoReplacement() {
        testMessage("hey", "Hello, " + getTestBot().getNick());
    }

    public void randomList() {
        testMessageList("coin", Arrays.asList("heads", "tails"));
    }

    @Test(enabled = false)
    public void guessFactoid() {
        testMessage("bre", "I guess the factoid 'label line breaks' might be appropriate:");
    }

    public void noGuess() {
        testMessage("apiz", getTestBot().getNick() + ", I have no idea what apiz is.");
    }

    public void action() {
        testMessage("hug " + getTestBot().getNick(), " hugs " + getTestBot().getNick());
    }

    @Test
    public void channelMessage() throws IOException {
        testChannelMessage("pong is");
    }

    @Test
    public void tell() {
        final String nick = TARGET_TEST_BOT;
        testMessage(String.format("tell %s about hey", nick), "Hello, " + nick);
        testMessage(String.format("tell %s about camel I am a test", nick), nick + ", IAmATest");
        testMessage(String.format("tell %s about url I am a test", nick), String.format("%s, I+am+a+test", nick));
        testMessage(String.format("tell %s about javadoc String", nick), String.format("%s: http://is.gd/4ygdW [JDK: java.lang.String]", nick));
        testMessage(String.format("tell %s about stupid", nick), String.format("%s, what you've just said is one of the most"
            + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
            + " even close to anything that could be considered a rational thought. Everyone in this room is now"
            + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.", nick));

        sleep(6000);
        sendTell(String.format("~~ %s seeTest", nick), String.format("%s, I'm a reply!", nick));
        sendTell(String.format("~~ %s bobloblaw", nick), String.format("%s, I have no idea what bobloblaw is.", nick));
        sendTell(String.format("~~ %s api", nick), String.format("%s, api is http://java.sun.com/javase/current/docs/api/index.html", nick));
        sendTell(String.format("~~ %s camel I am a test 2", nick), String.format("%s, IAmATest2", nick));
        sendTell(String.format("~~ %s url I am a test 2", nick), String.format("%s, I+am+a+test+2", nick));
        sendTell(String.format("~~ %s javadoc String", nick), String.format("%s: http://is.gd/4ygdW [JDK: java.lang.String]", nick));
        sendTell(String.format("~~ %s stupid", nick), String.format("%s, what you've just said is one of the most"
            + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
            + " even close to anything that could be considered a rational thought. Everyone in this room is now"
            + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.", nick));

        sleep(6000);
        sendTell(String.format("~~%s seeTest", nick), String.format("%s, I'm a reply!", nick));
        sendTell(String.format("~~%s bobloblaw", nick), String.format("%s, I have no idea what bobloblaw is.", nick));
        sendTell(String.format("~~%s api", nick), String.format("%s, api is http://java.sun.com/javase/current/docs/api/index.html", nick));
        sendTell(String.format("~~%s camel I am a test 3", nick), String.format("%s, IAmATest3", nick));
        sendTell(String.format("~~%s url I am a test 3", nick), String.format("%s, I+am+a+test+3", nick));
        sendTell(String.format("~~%s javadoc String", nick), String.format("%s: http://is.gd/4ygdW [JDK: java.lang.String]", nick));
        sendTell(String.format("~~%s stupid", nick), String.format("%s, what you've just said is one of the most"
            + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
            + " even close to anything that could be considered a rational thought. Everyone in this room is now"
            + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.", nick));
    }

    private void sendTell(final String message, final String response) {
        final TestBot bot = getTestBot();
        bot.sendMessage(getJavabotChannel(), message);
        validateResponses(bot, response);
    }
}