package javabot.mock;

import javabot.BaseMessagingTest;
import org.testng.annotations.Test;

@Test(groups = {"operations"})
public class SayOperationsTest extends BaseMessagingTest {
    public void testSay() {
        testMessage("~say MAGNIFICENT", "MAGNIFICENT");
    }
}