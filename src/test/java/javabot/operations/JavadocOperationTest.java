package javabot.operations;

import java.util.Arrays;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created Aug 9, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
@Test(dependsOnGroups = {"javadoc"})
public class JavadocOperationTest extends BaseOperationTest {
    @Autowired
    private ApiDao apiDao;
    @Autowired
    private ClazzDao clazzDao;

    public void string() {
        final String response = "http://is.gd/6UoM (JDK)";
        final String errorMessage = "Should have found class";
        final BotOperation operation = getOperation();

        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "javadoc String");
        List<Message> results = operation.handleMessage(event);
        Assert.assertTrue(!results.isEmpty());
        Assert.assertEquals(results.get(0).getMessage(), response, errorMessage);

        event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "javadoc java.lang.String");
        results = operation.handleMessage(event);
        Assert.assertTrue(!results.isEmpty());
        Assert.assertEquals(results.get(0).getMessage(), response, errorMessage);
    }

    public void methods() {
        testOperation("javadoc String.split(String)", "http://is.gd/eOPq (JDK)");
        testOperation("javadoc String.split(java.lang.String)", "http://is.gd/eOPq (JDK)");

        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "javadoc String.split(*)");
        final Message message = getOperation().handleMessage(event).get(0);
        final String string = message.getMessage();
        final String[] results = string.split(",");
        Assert.assertEquals(results.length, 2);
        final List<String> responses = Arrays.asList("http://is.gd/eOPq (JDK)", "http://is.gd/eOPr (JDK)");
        Assert.assertTrue(responses.contains(results[0].trim()));
        Assert.assertTrue(responses.contains(results[1].trim()));

    }

    @Override
    protected BotOperation createOperation() {
        return new JavadocOperation(getJavabot());
    }
}
