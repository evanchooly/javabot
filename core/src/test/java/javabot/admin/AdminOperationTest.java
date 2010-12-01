package javabot.admin;

import java.util.Iterator;
import java.util.List;

import javabot.Message;
import javabot.dao.ConfigDao;
import javabot.operations.BaseOperationTest;
import javabot.operations.BotOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class AdminOperationTest extends BaseOperationTest {
    @Autowired
    ConfigDao dao;

    public void disableOperations() {
        final List<Message> messages = sendMessage("~admin listOperations");
        System.out.println("messages = " + messages);
        final List<BotOperation> list = BotOperation.list();
        for (final String name : messages.get(3).getMessage().split(",")) {
            final String opName = name.trim().split(" ")[0];
            sendMessage("~admin disableOperation --name=" + opName.trim());

            if(!getJavabot().getOperation(opName).isStandardOperation()) {
                Assert.assertFalse(findOperation(opName));
            }
        }
    }

    public boolean findOperation(final String name) {
        boolean found = false;
        final Iterator<BotOperation> it = getJavabot().getOperations();
        while (it.hasNext() && !(found = it.next().getName().equals(name))) {
        }
        return found;
    }

    @Test(dependsOnMethods = {"disableOperations"})
    public void enableOperations() {
        final String message = sendMessage("~admin listOperations").get(1).getMessage();
        for (final String name : message.split(",")) {
            final String opName = name.trim().split(" ")[0];
            sendMessage("~admin enableOperation --name=" + opName);
            Assert.assertTrue(findOperation(opName), "Looking for " + opName);
        }
    }
}
