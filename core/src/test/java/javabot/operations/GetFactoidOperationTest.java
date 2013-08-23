package javabot.operations;

import java.io.IOException;
import java.util.Arrays;
import javax.inject.Inject;

import javabot.BaseTest;
import javabot.dao.FactoidDao;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class GetFactoidOperationTest extends BaseOperationTest {
  private static final String REPLY_VALUE = "I'm a reply!";
  @Inject
  private FactoidDao factoidDao;

  @BeforeClass
  public void createGets() {
    deleteFactoids();
    final String user = TEST_USER.getNick();
    factoidDao.addFactoid(user, "api", "http://java.sun.com/javase/current/docs/api/index.html");
    factoidDao.addFactoid(user, "replyTest", "<reply>I'm a reply!");
    factoidDao.addFactoid(user, "stupid", "<reply>$who, what you've just said is one of the most"
        + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
        + " even close to anything that could be considered a rational thought. Everyone in this room is now"
        + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.");
    factoidDao.addFactoid(user, "seeTest", "<see>replyTest");
    factoidDao.addFactoid(user, "noReply", "I'm a reply!");
    factoidDao.addFactoid(user, "replace $1", "<reply>I replaced you $1");
    factoidDao.addFactoid(user, "camel $^", "<reply>$^");
    factoidDao.addFactoid(user, "url $+", "<reply>$+");
    factoidDao.addFactoid(user, "hey", "<reply>Hello, $who");
    factoidDao.addFactoid(user, "coin", "<reply>(heads|tails)");
    factoidDao.addFactoid(user, "hug $1", "<action> hugs $1");
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
      factoidDao.delete(TEST_USER.getNick(), key);
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
    testMessage("~apiz", BaseTest.TEST_USER + ", what does that even *mean*?");
  }

  public void action() {
    testMessage("~hug " + BaseTest.TEST_USER, " hugs " + BaseTest.TEST_USER);
  }

  @Test
  public void tell() {
    testMessage(String.format("~tell %s about hey", TEST_USER), "Hello, " + TEST_USER);
    testMessage(String.format("~tell %s about camel I am a test", TEST_USER), TEST_USER + ", IAmATest");
    testMessage(String.format("~tell %s about url I am a test", TEST_USER), String.format("%s, I+am+a+test",
        TEST_USER));
    testMessage(String.format("~tell %s about stupid", TEST_USER),
        String.format("%s, what you've just said is one of the most"
            + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
            + " even close to anything that could be considered a rational thought. Everyone in this room is now"
            + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.",
            TEST_USER));
    sleep(6000);
    testMessage(String.format("~~ %s seeTest", TEST_USER), String.format("%s, I'm a reply!", TEST_USER));
    testMessage(String.format("~~ %s bobloblaw", TEST_USER),
        String.format("%s, what does that even *mean*?", TEST_USER));
    testMessage(String.format("~~ %s api", TEST_USER),
        String.format("%s, api is http://java.sun.com/javase/current/docs/api/index.html", TEST_USER));
    validate("camel I am a test 2", "IAmATest2");
    testMessage(String.format("~~ %s url I am a test 2", TEST_USER), String.format("%s, I+am+a+test+2", TEST_USER));
//    scanForResponse(String.format("~~ %s javadoc String", TEST_USER), "[JDK: java.lang.String]");
//    scanForResponse(String.format("~~ %s javadoc String", new IrcUser("jimbob")), "jimbob");
    testMessage(String.format("~~ %s stupid", TEST_USER),
        String.format("%s, what you've just said is one of the most"
            + " insanely idiotic things I have ever heard. At no point in your rambling, incoherent response were you"
            + " even close to anything that could be considered a rational thought. Everyone in this room is now"
            + " dumber for having listened to it. I award you no points, and may God have mercy on your soul.",
            TEST_USER));
    testMessage(String.format("~~%s seeTest", TEST_USER), String.format("%s, I'm a reply!", TEST_USER));
    testMessage(String.format("~~%s bobloblaw", TEST_USER), String.format("%s, what does that even *mean*?",
        TEST_USER));
    testMessage(String.format("~~%s api", TEST_USER),
        String.format("%s, api is http://java.sun.com/javase/current/docs/api/index.html", TEST_USER));
    testMessage(String.format("~~%s camel I am a test 3", TEST_USER), String.format("%s, IAmATest3", TEST_USER));
    testMessage(String.format("~~%s url I am a test 3", TEST_USER), String.format("%s, I+am+a+test+3", TEST_USER));
    validate("stupid", "what you've just said is one of the most insanely idiotic things I have ever heard. At no" +
        " point in your rambling, incoherent response were you even close to anything that could be considered" +
        " a rational thought. Everyone in this room is now dumber for having listened to it. I award you no" +
        " points, and may God have mercy on your soul.");
  }

  private void validate(final String factoid, final String response) {
    testMessage(String.format("~~ %s " + factoid, TEST_USER), String.format("%s, " + response, TEST_USER));
  }
}