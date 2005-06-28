package javabot.operations;

import java.util.List;
import java.io.IOException;

import javabot.BotEvent;
import javabot.Message;
import org.testng.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created Jun 28, 2005
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public abstract class BaseOperationTest {
    protected static final String SENDER = "cheeser";
    private static final String CHANNEL = "#test";
    private static final String LOGIN = "";
    private static final String HOSTNAME = "localhost";
    private static Log log = LogFactory.getLog(BaseOperationTest.class);

    protected void testOperation(String message, String response, String errorMessage) {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, message);
        List<Message> results = getOperation().handleMessage(event);
        Message result = results.get(0);
        log.debug("response = " + response);
        log.debug("result = " + result.getMessage());
        Assert.assertEquals(response, result.getMessage(), errorMessage);
    }

    protected abstract BotOperation getOperation();
}
