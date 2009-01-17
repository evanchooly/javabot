package javabot.operations;

import java.io.IOException;
import java.util.List;

import javabot.Action;
import javabot.BotEvent;
import javabot.Message;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class GetFactoidOperationTest extends BaseOperationTest {
    private static final String PBV_VALUE = "Java only supports pass by value, *NOT* pass by reference. (References"
        + " to objects are passed by value). For more information please see the following link:"
        + " http://javachannel.net/wiki/pmwiki.php/FAQ/PassingVariables";
    private static final String ERROR_MESSAGE = "Should have found the factoid";
    @Autowired
    private FactoidDao factoidDao;

    @Override
    protected BotOperation createOperation() {
        return new GetFactoidOperation(getJavabot());
    }

    public void straightGets() throws IOException {
        testOperation("api", getFoundMessage("api", "http://java.sun.com/javase/current/docs/api/index.html"), ERROR_MESSAGE);
    }

    public void replyGets() {
        testOperation("pass by value", PBV_VALUE, ERROR_MESSAGE);
    }

    public void seeGets() {
        testOperation("pebcak", getFoundMessage("pebcak",
            "where the (P)roblem (E)xists (B)etween (C)hair (A)nd (K)eyboard. These sorts of problems"
                + " infuriate employees of companies, but is a contractor's dream.  This is because it is like writing"
                + " an open check to hourly based people."),
            ERROR_MESSAGE);
    }

    public void seeReplyGets() {
        testOperation("pbv", PBV_VALUE, ERROR_MESSAGE);
    }

    public void parameterReplacement() {
        testOperation("bomb cheeser", "<randyjackson>cheeser, you're the bomb, dog!</randyjackson>", ERROR_MESSAGE);
    }

    public void whoReplacement() {
        testOperation("hey", "Hello, " + SENDER, ERROR_MESSAGE);
    }

    public void randomList() {
        testOperation("coin", new String[]{"heads", "tails"}, ERROR_MESSAGE);
    }

    public void questionFactoid() {
        testOperation("how to use spinners?", getFoundMessage("how to use spinners",
            "http://java.sun.com/docs/books/tutorial/uiswing/components/spinner.html"), ERROR_MESSAGE);
    }

    @Test(enabled = false)
    public void guessFactoid() {
        testOperation("bre", "I guess the factoid 'label line breaks' might be appropriate:", ERROR_MESSAGE);
    }

    public void noGuess() {
        testOperation("apiz", SENDER + ", I have no idea what apiz is.", ERROR_MESSAGE);
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
