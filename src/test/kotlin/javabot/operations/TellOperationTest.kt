package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseMessagingTest
import javabot.dao.FactoidDao
import javabot.model.criteria.LogsCriteria
import org.testng.Assert
import org.testng.annotations.Test
import java.lang.String.format
import javax.inject.Inject

@Test
public class TellOperationTest : BaseMessagingTest() {
    @Inject
    lateinit val dao: FactoidDao


    public fun shortcut() {
        logsDao.deleteAllForChannel(javabotChannel.name)
        val nick = javabot.get().getNick()
        dao.delete(nick, "shortcut")
        try {
            val message = "I'm a shortcut response"
            testMessage("~shortcut is <reply>" + message, ok)
            testMessage(format("~~ %s shortcut", testUser),
                  format("%s, %s", testUser, message))
            val criteria = LogsCriteria(datastore)
            criteria.message(format("%s, %s", testUser.nick, message))
            val logs = criteria.query().get()
            Assert.assertEquals(logs.nick, javabot.get().getNick())
        } finally {
            dao.delete(nick, "shortcut")
        }
    }

    public fun unknownTell() {
        dao.delete(javabot.get().getNick(), "shortcut")
        testMessage(format("~~ %s shortcut", testUser), Sofia.unhandledMessage(testUser.nick))
    }
}
