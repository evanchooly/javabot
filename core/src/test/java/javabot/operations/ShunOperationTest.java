package javabot.operations;

import javabot.BaseTest;
import org.testng.annotations.Test;

/**
 * Created Feb 26, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test
public class ShunOperationTest extends BaseOperationTest {
    public void shunMe() throws InterruptedException {
        sendMessage("~forget shunHey");
        try {
            sendMessage("~shunHey is <reply>shunHey");
            scanForResponse("~shun " + BaseTest.TEST_USER + " 10", BaseTest.TEST_USER + " is shunned until");
            testMessage("~ping");
            Thread.sleep(10000);
            testMessage("~shunHey", "shunHey");
        } finally {
            sendMessage("~forget shunHey");
        }
    }
}
