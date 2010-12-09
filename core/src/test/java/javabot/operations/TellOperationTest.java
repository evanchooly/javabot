package javabot.operations;

import javabot.BaseTest;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

@Test
public class TellOperationTest extends BaseOperationTest {
    @Autowired
    private FactoidDao dao;

    public void shortcut() {
        dao.delete(getJavabot().getNick(), "shortcut");
        try {
            final String nick = getJavabot().getNick();
            final String message = "I'm a shortcut response";
            testMessage("~shortcut is <reply>" + message, ok);
            testMessage(String.format("~~ %s shortcut", BaseTest.TEST_USER),
                String.format("%s, %s", BaseTest.TEST_USER, message));
        } finally {
            dao.delete(getJavabot().getNick(), "shortcut");
        }
    }
}
