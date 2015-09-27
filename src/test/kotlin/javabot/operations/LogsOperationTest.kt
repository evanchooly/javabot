package javabot.operations

import javabot.BaseMessagingTest
import javabot.Message
import javabot.model.Logs
import org.pircbotx.User
import org.testng.Assert
import org.testng.annotations.Test
import java.util.UUID

public class LogsOperationTest : BaseMessagingTest() {
    @Test
    @Throws(Exception::class)
    public fun testChannelLogs() {
        val query = datastore.createQuery(Logs::class.java)
        datastore.delete(query)
        // Add a known and unique message to the logs so we can validate that we are testing against new data
        val uuid = UUID.randomUUID().toString()
        sendMessage(uuid)
        val list = sendMessage("~logs")
        Assert.assertFalse(list.isEmpty())
        val listSize = list.size()
        Assert.assertTrue(list.get(listSize - 2).contains(uuid))
        Assert.assertTrue(list.get(listSize - 1).contains("~logs"))
    }

    @Test
    @Throws(Exception::class)
    public fun testNickSpecificLogsWhenNoLogsForNick() {
        // We generate unique user names so that existing data in the DB doesn't interfere with this unit test
        val uuid = UUID.randomUUID().toString()
        val list = sendMessage("~logs " + uuid)
        val listSize = list.size()
        Assert.assertEquals(listSize, 1)
        val msg = list.get(listSize - 1)
        Assert.assertTrue(msg.contains("No logs found for nick: " + uuid))
    }

    @Test
    @Throws(Exception::class)
    public fun testNickSpecificLogsWhenLogs() {
        val uuid = UUID.randomUUID().toString()
        val user = object : TestUser(uuid) {

        }
        javabot.get().processMessage(Message(javabotChannel, user, "Hello I'm " + uuid))
        val list = sendMessage("~logs " + uuid)
        val listSize = list.size()
        Assert.assertEquals(listSize, 1)
        Assert.assertTrue(list.get(listSize - 1).contains(uuid))
    }

    private open inner class TestUser(nick: String) : User(ircBot.get(), ircBot.get().userChannelDao, nick)
}
