package javabot.dao

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.model.Channel
import javabot.model.JavabotUser
import javabot.model.Logs
import javabot.model.Logs.Type
import javabot.model.criteria.LogsCriteria
import org.mongodb.morphia.Datastore
import org.testng.Assert
import org.testng.annotations.Test
import java.time.LocalDateTime
import javax.inject.Inject

class LogsDaoTest @Inject constructor(val ds: Datastore) : BaseTest() {
    companion object {
        val CHANNEL_NAME: String = "#watercooler"
    }

    @Test fun seen() {
        val logsCriteria = LogsCriteria(ds)
        logsCriteria.channel(CHANNEL_NAME)
        logsCriteria.delete()
        channelDao.delete(channelDao.get(CHANNEL_NAME))
        val channel = Channel()
        channel.name = CHANNEL_NAME
        channel.logged = true
        channelDao.save(channel)
        logsDao.logMessage(Type.MESSAGE, channel, JavabotUser("ChattyCathy", "ChattyCathy", "localhost"), "test message")

        Assert.assertNotNull(logsDao.getSeen(channel.name, "chattycathy"))
        Assert.assertFalse(logsDao.findByChannel(channel.name, LocalDateTime.now(), false).isEmpty())
        Assert.assertTrue(logsDao.findByChannel(channel.name, LocalDateTime.now().minusDays(1), false).isEmpty())
    }

    @Test fun channelEvents() {
        val chanName = "##testChannel"
        channelDao.delete(chanName)
        val channel = channelDao.create(chanName, true, null)
        logsDao.deleteAllForChannel(chanName)

        logsDao.logMessage(Logs.Type.PART, channel, TEST_USER, Sofia.userParted(TEST_USER.nick, "i'm out of here!"))

        val logs = logsDao.findByChannel(chanName, LocalDateTime.now(), true)

        Assert.assertFalse(logs.isEmpty(), "Should have one log entry")
        Assert.assertEquals(logs[0].message, Sofia.userParted(TEST_USER.nick, "i'm out of here!"))
    }
}
