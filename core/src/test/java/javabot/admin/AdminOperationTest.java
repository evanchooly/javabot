package javabot.admin;

import java.util.Iterator;
import java.util.List;

import javabot.Message;
import javabot.commands.AdminCommand;
import javabot.dao.ConfigDao;
import javabot.operations.BaseOperationTest;
import javabot.operations.BotOperation;
import javabot.operations.StandardOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class AdminOperationTest extends BaseOperationTest {
    @Autowired
    ConfigDao dao;

    public void disableOperations() {
        final List<Message> messages = sendMessage("~admin listOperations");
        for (final String name : messages.get(3).getMessage().split(",")) {
            final String opName = name.trim().split(" ")[0];
            sendMessage("~admin disableOperation --name=" + opName.trim());
            final BotOperation operation = findOperation(opName);
            Assert.assertTrue(operation == null || operation instanceof AdminCommand || operation instanceof StandardOperation);
        }
    }

    public BotOperation findOperation(final String name) {
        final Iterator<BotOperation> it = getJavabot().getOperations();
        while (it.hasNext()) {
            final BotOperation op = it.next();
            if(op.getName().equals(name)) {
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
