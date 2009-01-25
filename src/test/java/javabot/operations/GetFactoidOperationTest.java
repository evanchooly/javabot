package javabot.operations;

import java.io.IOException;
import java.util.List;

import javabot.Action;
import javabot.BotEvent;
import javabot.Message;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class GetFactoidOperationTest extends BaseOperationTest {
    private static final String REPLY_VALUE = "I'm a reply!";
    private static final String ERROR_MESSAGE = "Should have found the factoid";
    @Autowired
    private FactoidDao factoidDao;

    @Override
    protected BotOperation createOperation() {
        return new GetFactoidOperation(getJavabot());
    }

    @BeforeClass
    public void createGets() {
        deleteFactoids();
        factoidDao.addFactoid(SENDER, "api", "http://java.sun.com/javase/current/docs/api/index.html");
        factoidDao.addFactoid(SENDER, "replyTest", "<reply>I'm a reply!");
        factoidDao.addFactoid(SENDER, "seeTest", "<see>replyTest");
        factoidDao.addFactoid(SENDER, "noReply", "I'm a reply!");
        factoidDao.addFactoid(SENDER, "replace $1", "<reply>I replaced you " + SENDER);
        factoidDao.addFactoid(SENDER, "hey", "<reply>Hello, " + SENDER);
        factoidDao.addFactoid(SENDER, "coin", "<reply>(heads|tails)");
        factoidDao.addFactoid(SENDER, "hug $1", "<action> hugs $1");
    }

    @AfterClass
    public void deleteFactoids() {
        delete("api");
        delete("replyTest");
        delete("seeTest");
        delete("noReply");
        delete("replace $1");
        delete("hey");
        delete("coin");
        delete("hug $1");
    }

    private void delete(final String key) {
        while (factoidDao.hasFactoid(key)) {
            factoidDao.delete(SENDER, key);
        }
    }

    public void straightGets() throws IOException {
        testOperation("api", getFoundMessage("api", "http://java.sun.com/javase/current/docs/api/index.html"));
    }

    public void replyGets() {
        testOperation("replyTest", REPLY_VALUE);
    }

    public void seeGets() {
        testOperation("seeTest", REPLY_VALUE);
    }

    public void seeReplyGets() {
        testOperation("seeTest", REPLY_VALUE);
    }

    public void parameterReplacement() {
        testOperation("replace " + SENDER, "I replaced you " + SENDER);
    }

    public void whoReplacement() {
        testOperation("hey", "Hello, " + SENDER);
    }

    public void randomList() {
        testOperation("coin", new String[]{"heads", "tails"}, ERROR_MESSAGE);
    }

    @Test(enabled = false)
    public void guessFactoid() {
        testOperation("bre", "I guess the factoid 'label line breaks' might be appropriate:");
    }

    public void noGuess() {
        testOperation("apiz", SENDER + ", I have no idea what apiz is.");
    }

    public void badRandom() {
        final GetFactoidOperation operation = (GetFactoidOperation) getOperation();
        String message = "(1|2";
        Assert.assertEquals(operation.processRandomList(message), message, "Should just return message");
        message = "(1)";
        Assert.assertEquals(operation.processRandomList(message), "(1)", "Should just return message");

    }

    public void action() {
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "hug " + SENDER);
        final List<Message> results = getOperation().handleMessage(event);
        final Message result = results.get(0);
        Assert.assertTrue(result instanceof Action, "Should be an action.");
    }

    @Test
    public void channelMessage() throws IOException {
        final BotEvent event = new BotEvent("##javabot", SENDER, "", "localhost", "pong is");
        Assert.assertEquals(getOperation().handleChannelMessage(event).size(), 0, "Should be an empty list");
    }

}
