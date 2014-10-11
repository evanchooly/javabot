package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.dao.FactoidDao;
import org.testng.annotations.Test;

import javax.inject.Inject;

@Test
public class TellOperationTest extends BaseOperationTest {
    @Inject
    private FactoidDao dao;

    public void shortcut() {
        final String nick = getJavabot().getNick();
        dao.delete(nick, "shortcut");
        try {
            final String message = "I'm a shortcut response";
            testMessage("~shortcut is <reply>" + message, ok);
            testMessage(String.format("~~ %s shortcut", getTestUser()),
                        String.format("%s, %s", getTestUser(), message));
        } finally {
            dao.delete(nick, "shortcut");
        }
    }

    public void unknownTell() {
        dao.delete(getJavabot().getNick(), "shortcut");
        testMessage(String.format("~~ %s shortcut", getTestUser()), Sofia.unhandledMessage(getTestUser().getNick()));
    }
}
