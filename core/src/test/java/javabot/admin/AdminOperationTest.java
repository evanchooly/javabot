package javabot.admin;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import javabot.Message;
import javabot.commands.AdminCommand;
import javabot.dao.ConfigDao;
import javabot.operations.BaseOperationTest;
import javabot.operations.BotOperation;
import javabot.operations.StandardOperation;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class AdminOperationTest extends BaseOperationTest {
  @Inject
  ConfigDao dao;

  public void disableOperations() {
    final List<Message> messages = sendMessage("~admin listOperations");
    List<String> disabled = new ArrayList<>();
    try {
      for (final String name : messages.get(3).getMessage().split(",")) {
        final String opName = name.trim().split(" ")[0].trim();
        disabled.add(opName);
        sendMessage("~admin disableOperation --name=" + opName);
        final BotOperation operation = findOperation(opName);
        Assert.assertTrue(
            operation == null || operation instanceof AdminCommand || operation instanceof StandardOperation);
      }
    } finally {
      for (String name : disabled) {
        sendMessage("~admin enableOperation --name=" + name);
      }
    }
  }

  public BotOperation findOperation(final String name) {
    for (BotOperation op : getJavabot().getAllOperations()) {
      if (op.getName().equals(name)) {
        return op;
      }
    }
    return null;
  }

  @Test(dependsOnMethods = {"disableOperations"})
  public void enableOperations() {
    final String message = sendMessage("~admin listOperations").get(1).getMessage();
    for (final String name : message.split(",")) {
      final String opName = name.trim().split(" ")[0];
      sendMessage("~admin enableOperation --name=" + opName);
      Assert.assertTrue(findOperation(opName) != null, "Looking for " + opName);
    }
  }
}
