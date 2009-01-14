package javabot.operations;

import java.util.List;

import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.BotEvent;
import javabot.Message;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.unitils.spring.annotation.SpringBeanByType;

/**
 * Created Aug 9, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
@Test
public class JavadocOperationTest extends BaseOperationTest {
    @SpringBeanByType
    private ApiDao apiDao;
    @SpringBeanByType
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
        testOperation("javadoc String.split(*)", "http://is.gd/eOPq (JDK), http://is.gd/eOPr (JDK)", "Should have found method");
        testOperation("javadoc String.split(String)", "http://is.gd/eOPq (JDK)", "Should have found method");
        testOperation("javadoc String.split(java.lang.String)", "http://is.gd/eOPq (JDK)", "Should have found method");

    }

    @Override
    protected BotOperation createOperation() {
        return new JavadocOperation(getJavabot());
    }
}
