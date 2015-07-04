package javabot.operations;

import javabot.BaseMessagingTest;
import org.testng.annotations.Test;

import java.util.Date;

@Test(groups = "operations")
public class LiteralOperationTest extends BaseMessagingTest {
  @Test
  public void testMissingFactoid() {
    String factoidName = "foo" + new Date().getTime();
    testMessage("~literal " + factoidName,
        "I have no factoid called \"" + factoidName + "\"");
  }

  // TODO needs "existing factoid test"
}
