package javabot.admin;

import com.jayway.awaitility.Awaitility;
import javabot.Messages;
import javabot.commands.AdminCommand;
import javabot.operations.BaseOperationTest;
import javabot.operations.BotOperation;
import javabot.operations.StandardOperation;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Test
public class AdminOperationTest extends BaseOperationTest {

    @Inject
    private Messages messages;

    public void disableOperations() {
        final Messages messages = sendMessage("~admin listOperations");
        List<String> disabled = new ArrayList<>();
        try {
            for (final String name : messages.get(2).split(",")) {
                final String opName = name.trim().split(" ")[0].trim();
                disabled.add(opName);
                sendMessage("~admin disableOperation --name=" + opName);
                final BotOperation operation = findOperation(opName);
                Assert.assertTrue(operation == null || operation instanceof AdminCommand || operation instanceof StandardOperation);
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
        Messages list = sendMessage("~admin listOperations");
        final String message = list.get(2);
        for (final String name : message.split(",")) {
            final String opName = name.trim().split(" ")[0];
            sendMessage("~admin enableOperation --name=" + opName);
            Awaitility.await("~admin enableOperation --name=" + opName)
                      .atMost(60, TimeUnit.SECONDS)
                      .until(() -> findOperation(opName) != null);
            messages.get();
        }
    }
}
