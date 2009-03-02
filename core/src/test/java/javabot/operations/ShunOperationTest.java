package javabot.operations;

import javabot.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created Feb 26, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test
public class ShunOperationTest extends BaseOperationTest {
    public void shunMe() throws InterruptedException {
        final TestBot testBot = getTestBot();
        testBot.sendMessage(getJavabotChannel(), getJavabot().getNick() + " shun " + testBot.getNick() + " 10");
        waitForResponses(testBot, 1);
        final Response response = testBot.getOldestResponse();
        Assert.assertTrue(response.getMessage().startsWith(testBot.getNick() + " is shunned until"),
            "TestBot should have been shunned");

        testBot.sendMessage(getJavabotChannel(), getJavabot().getNick() + " shun " + testBot.getNick());
        try {
            waitForResponses(testBot, 1);
            Assert.fail("bot should be shunned");
        } catch (AssertionError e) {
            Thread.sleep(5000);
        }
        
        testBot.sendMessage(getJavabotChannel(), getJavabot().getNick() + " shun " + testBot.getNick());
        waitForResponses(testBot, 1);
    }
}
