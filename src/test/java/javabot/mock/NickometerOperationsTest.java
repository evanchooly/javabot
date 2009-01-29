package javabot.mock;

import javabot.operations.BaseOperationTest;
import javabot.operations.BotOperation;
import javabot.operations.NickometerOperation;
import org.testng.annotations.Test;

@Test(groups = {"operations"})
public class NickometerOperationsTest extends BaseOperationTest {
    public BotOperation createOperation() {
        return new NickometerOperation(getJavabot());
    }

    public void testNick() {
        testMessage("nickometer MAGNIFICENT", "The nick MAGNIFICENT is 95% lame.");
    }

    public void testNickCh33s3r() {
        testMessage("nickometer ch33s3r", "The nick ch33s3r is 65% lame.");
    }
}