package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ChangesDao;
import javabot.dao.FactoidDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

import java.io.IOException;
import java.util.List;

@Test(groups = {"operations"})

public class GetFactoidOperationTest2 extends BaseOperationTest2 {
    private static Log log = LogFactory.getLog(GetFactoidOperationTest2.class);
    private static final String PBV_VALUE = "Java only supports pass by value, not pass by reference (references to objects are passed by value).  See http://tinyurl.com/ynr5d3, http://tinyurl.com/ywlv6d (especially http://tinyurl.com/yvppac), and http://tinyurl.com/4wgdh (search for \"Passing Reference Data Type Arguments\")";
    private static final String ERROR_MESSAGE = "Should have found the factoid";

    @SpringBeanByType
    private FactoidDao factoidDao;

    @SpringBeanByType
    private ChangesDao changesDao;

    protected GetFactoidOperation getOperation2() {
        return new GetFactoidOperation(factoidDao);
    }

    public void straightGets() throws IOException {
        testOperation2("api", getFoundMessage2("api", "http://java.sun.com/javase/6/docs/api/"), ERROR_MESSAGE);
    }

    public void replyGets() {
        testOperation2("pass by value", PBV_VALUE, ERROR_MESSAGE);
    }

    public void seeGets() {
        testOperation2("pebcak", getFoundMessage2("pebcak", "Problem Exists Between Keyboard " + "And Chair"), ERROR_MESSAGE);
    }

    public void seeReplyGets() {
        testOperation2("pbv", PBV_VALUE, ERROR_MESSAGE);
    }

    public void parameterReplacement() {
        testOperation2("bomb cheeser", "drops a humongous exploding turd on cheeser", ERROR_MESSAGE);
    }

    public void whoReplacement() {
        testOperation2("hey", "Hello, " + SENDER, ERROR_MESSAGE);
    }

    public void randomList() {
        testOperation2("coin", new String[]{"heads", "tails"}, ERROR_MESSAGE);
    }

    public void questionFactoid() {
        testOperation2("how to use spinners?", getFoundMessage2("how to use spinners", "http://java.sun.com/docs/books/tutorial/uiswing/components/spinner.html"), ERROR_MESSAGE);
    }

    public void guessFactoid() {
        testOperation2("bre", "I guess the factoid 'label line breaks' might be appropriate:", ERROR_MESSAGE);
    }

    public void noGuess() {
        testOperation2("apiz", SENDER + ", I have no idea what apiz is.", ERROR_MESSAGE);
    }

    public void badRandom() {
        GetFactoidOperation operation = getOperation2();
        String message = "(1|2";
        Assert.assertEquals(operation.processRandomList(message), message, "Should just return message");

        message = "(1)";
        Assert.assertEquals(operation.processRandomList(message), "(1)", "Should just return message");

    }

    public void action() {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "hug " + SENDER);
        List<Message> results = getOperation2().handleMessage(event);
        Message result = results.get(0);
        Assert.assertEquals(result.isAction(), true, "Should be an action.");
    }

    public void channelMessage() throws IOException {
        BotEvent event = new BotEvent("#test", SENDER, "", "localhost", "pong is");
        Assert.assertEquals(getOperation2().handleChannelMessage(event).size(), 0, "Should be an empty list");
    }

}