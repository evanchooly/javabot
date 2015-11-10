package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.dao.FactoidDao
import javabot.model.criteria.LogsCriteria
import org.testng.Assert
import org.testng.annotations.Test
import javax.inject.Inject

@Test
public class TellOperationTest : BaseTest() {
    @Inject
    protected lateinit var dao: FactoidDao
    @Inject
    private lateinit var operation: GetFactoidOperation

    public fun shortcut() {
        logsDao.deleteAllForChannel(testChannel.name)
        val nick = bot.get().getNick()
        dao.delete(nick, "shortcut")
        try {
            val message = "I'm a shortcut response"
            var response = operation.handleMessage(message("shortcut is <reply>" + message))
            Assert.assertEquals(response[0].value, ok)
            response = operation.handleMessage(message("~ ${testUser} shortcut"))
            Assert.assertEquals(response[0].value, "${testUser}, ${message}")
            val criteria = LogsCriteria(datastore)
            criteria.message("${testUser.nick}, ${message}")
            val logs = criteria.query().get()
            Assert.assertEquals(logs.nick, bot.get().getNick())
        } finally {
            dao.delete(nick, "shortcut")
        }
    }

    public fun unknownTell() {
        dao.delete(bot.get().getNick(), "shortcut")
        var response = operation.handleMessage(message("~ ${testUser} shortcut"))
        Assert.assertEquals(response[0].value, Sofia.unhandledMessage(testUser.nick))
    }
}
