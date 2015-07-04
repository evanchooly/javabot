package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.BaseMessagingTest;
import javabot.dao.FactoidDao;
import javabot.dao.LogsDao;
import javabot.model.Logs;
import javabot.model.criteria.LogsCriteria;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static java.lang.String.*;

@Test
public class TellOperationTest extends BaseMessagingTest {
    @Inject
    private FactoidDao dao;

    @Inject
    private LogsDao logsDao;

    public void shortcut() {
        logsDao.deleteAllForChannel(getJavabotChannel().getName());
        final String nick = getJavabot().getNick();
        dao.delete(nick, "shortcut");
        try {
            final String message = "I'm a shortcut response";
            testMessage("~shortcut is <reply>" + message, ok);
            testMessage(format("~~ %s shortcut", getTestUser()),
                        format("%s, %s", getTestUser(), message));
            LogsCriteria criteria = new LogsCriteria(getDatastore());
            criteria.message(format("%s, %s", getTestUser().getNick(), message));
            final Logs logs = criteria.query().get();
            Assert.assertEquals(logs.getNick(), getJavabot().getNick());
        } finally {
            dao.delete(nick, "shortcut");
        }
    }

    public void unknownTell() {
        dao.delete(getJavabot().getNick(), "shortcut");
        testMessage(format("~~ %s shortcut", getTestUser()), Sofia.unhandledMessage(getTestUser().getNick()));
    }
}
