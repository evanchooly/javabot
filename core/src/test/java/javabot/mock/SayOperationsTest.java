package javabot.mock;

import javabot.operations.BaseOperationTest;
import org.testng.annotations.Test;

@Test(groups = {"operations"})
public class SayOperationsTest extends BaseOperationTest {
    public void testSay() {
        testMessage("~say MAGNIFICENT", "MAGNIFICENT");
    }
}