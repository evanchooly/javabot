package javabot.operations;

import javabot.BaseTest;
import org.testng.annotations.Test;

@Test(enabled = false)
public class ShunOperationTest extends BaseOperationTest {
  public void shunMe() throws InterruptedException {
    sendMessage("~forget shunHey");
    try {
      sendMessage("~shunHey is <reply>shunHey");
      scanForResponse(String.format("~shun %s 5", getTestUser()), getTestUser() + " is shunned until");
      testMessage("~shunHey");
      Thread.sleep(5000);
      testMessage("~shunHey", "shunHey");
    } finally {
      sendMessage("~forget shunHey");
    }
  }
}
