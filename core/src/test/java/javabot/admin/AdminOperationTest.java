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
        final List<BotOperation> list = BotOperation.list();
        for (final String name : messages.get(3).getMessage().split(",")) {
            sendMessage("~admin disableOperation --name=" + name.trim());
            boolean standard = false;
            for (final BotOperation operation : list) {
                if (operation.getName().equals(name)) {
                    standard = operation.isStandardOperation();
                }
            }

            if(!standard) {
                Assert.assertFalse(findOperation(name));
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
        for (final String name : sendMessage("~admin listOperations").get(1).getMessage().split(",")) {
            sendMessage("~admin enableOperation --name=" + name.trim());
            Assert.assertTrue(findOperation(name), "Looking for " + name);
        }
    }
}
