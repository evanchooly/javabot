package javabot.operations;

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
        scanForResponse("~shun " + testBot.getNick() + " 10", testBot.getNick() + " is shunned until");
    }
}
