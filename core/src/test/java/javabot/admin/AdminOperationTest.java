package javabot.admin;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.sun.tools.doclets.formats.html.resources.standard;
import javabot.Javabot;
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
        for (final String name : messages.get(3).getMessage().split(",")) {
            sendMessage("~admin disableOperation --name=" + name.trim());
            final Iterator<BotOperation> it = getJavabot().getOperations();
            boolean standard = false;
            while(it.hasNext()) {
                final BotOperation operation = it.next();
                if(operation.getName().equals(name)) {
                    standard = operation.isStandardOperation();
                }
            }

            if(!standard) {
                Assert.assertFalse(dao.get().getOperations().contains(name));
            }
        }
    }

    @Test(dependsOnMethods = {"disableOperations"})
    public void enableOperations() {
        for (final String name : sendMessage("~admin listOperations").get(1).getMessage().split(",")) {
            sendMessage("~admin enableOperation --name=" + name.trim());
            Assert.assertTrue(dao.get().getOperations().contains(name));
        }
    }
}
