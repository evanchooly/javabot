package javabot.operations;

import java.io.IOException;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.unitils.spring.annotation.SpringBeanByType;
import javabot.dao.FactoidDao;
import javabot.dao.ChangesDao;
import javabot.BotEvent;

/**
 * Created Jun 30, 2005
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
@Test(groups = {"operations"})
public class ForgetFactoidOperationTest extends BaseOperationTest {
    private static Log log = LogFactory.getLog(ForgetFactoidOperationTest.class);

    @SpringBeanByType
    private FactoidDao factoidDao;

    @SpringBeanByType
    private ChangesDao changesDao;

    public void forgetFactoid() {
        if (!factoidDao.hasFactoid("afky")) {
            factoidDao.addFactoid(SENDER, "afky", "test", changesDao, "TEST");
        }
        testOperation2("forget afky", "I forgot about afky, " + SENDER + ".", "Should have forgotten factoid");
    }

    public void nonexistantFactoid() {
        testOperation2("forget asdfghjkl", "I never knew about asdfghjkl anyway, " + SENDER + ".", "Should not have known about factoid");
    }

    protected BotOperation getOperation2() {
        return new ForgetFactoidOperation(factoidDao, changesDao, "TEST");
    }

    public void channelMessage() throws IOException {
        BotEvent event = new BotEvent("#test", SENDER, "", "localhost", "pong is");
        Assert.assertEquals(getOperation2().handleChannelMessage(event).size(), 0, "Should be an empty list");
    }
}
