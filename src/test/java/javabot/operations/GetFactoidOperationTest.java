package javabot.operations;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.unitils.spring.annotation.SpringBeanByType;
import javabot.dao.FactoidDao;
import javabot.dao.ChangeDao;
import javabot.BotEvent;
import javabot.Message;

@Test(groups = {"operations"})

public class GetFactoidOperationTest extends BaseOperationTest {
    private static Log log = LogFactory.getLog(GetFactoidOperationTest.class);
    private static final String PBV_VALUE = "Java only supports pass by value, not pass by reference (references to objects are passed by value).  See http://tinyurl.com/ynr5d3, http://tinyurl.com/ywlv6d (especially http://tinyurl.com/yvppac), and http://tinyurl.com/4wgdh (search for \"Passing Reference Data Type Arguments\")";
    private static final String ERROR_MESSAGE = "Should have found the factoid";

    @SpringBeanByType
    private FactoidDao factoidDao;

    @SpringBeanByType
    private ChangeDao changeDao;

    protected GetFactoidOperation getOperation() {
        return new GetFactoidOperation(factoidDao);
    }

    public void straightGets() throws IOException {
        testOperation("api", getFoundMessage2("api", "http://java.sun.com/javase/6/docs/api/"), ERROR_MESSAGE);
    }

    public void replyGets() {
        testOperation("pass by value", PBV_VALUE, ERROR_MESSAGE);
    }

    public void seeGets() {
        testOperation("pebcak", getFoundMessage2("pebcak", "Problem Exists Between Keyboard " + "And Chair"),
            ERROR_MESSAGE);
    }

    public void seeReplyGets() {
        testOperation("pbv", PBV_VALUE, ERROR_MESSAGE);
    }

    public void parameterReplacement() {
        testOperation("bomb cheeser", "drops a humongous exploding turd on cheeser", ERROR_MESSAGE);
    }

    public void whoReplacement() {
        testOperation("hey", "Hello, " + SENDER, ERROR_MESSAGE);
    }

    public void randomList() {
        testOperation2("coin", new String[]{"heads", "tails"}, ERROR_MESSAGE);
    }

    public void questionFactoid() {
        testOperation("how to use spinners?", getFoundMessage2("how to use spinners", "http://java.sun.com/docs/books/tutorial/uiswing/components/spinner.html"),
            ERROR_MESSAGE);
    }

    public void guessFactoid() {
        testOperation("bre", "I guess the factoid 'label line breaks' might be appropriate:", ERROR_MESSAGE);
    }

    public void noGuess() {
        testOperation("apiz", SENDER + ", I have no idea what apiz is.", ERROR_MESSAGE);
    }

    public void badRandom() {
        GetFactoidOperation operation = getOperation();
        String message = "(1|2";
        Assert.assertEquals(operation.processRandomList(message), message, "Should just return message");

        message = "(1)";
        Assert.assertEquals(operation.processRandomList(message), "(1)", "Should just return message");

    }

    public void action() {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "hug " + SENDER);
        List<Message> results = getOperation().handleMessage(event);
        Message result = results.get(0);
        Assert.assertEquals(result.isAction(), true, "Should be an action.");
    }

    public void channelMessage() throws IOException {
        BotEvent event = new BotEvent("#test", SENDER, "", "localhost", "pong is");
        Assert.assertEquals(getOperation().handleChannelMessage(event).size(), 0, "Should be an empty list");
    }

}
