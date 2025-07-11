package javabot.dao

import com.antwerkz.sofia.Sofia
import dev.morphia.Datastore
import dev.morphia.query.filters.Filters
import java.time.LocalDateTime
import javabot.BaseTest
import javabot.model.Channel
import javabot.model.JavabotUser
import javabot.model.Logs
import javabot.model.Logs.Type
import javabot.model.Logs.Type.PART
import javax.inject.Inject
import org.testng.Assert
import org.testng.annotations.Test

class LogsDaoTest @Inject constructor(val ds: Datastore) : BaseTest() {
    companion object {
        val CHANNEL_NAME: String = "#watercooler"
    }

    @Test
    fun seen() {
        ds.find(Logs::class.java).filter(Filters.eq("channel", CHANNEL_NAME)).delete()
        channelDao.delete(channelDao.get(CHANNEL_NAME))
        val channel = Channel()
        channel.name = CHANNEL_NAME
        channel.logged = true
        channelDao.save(channel)
        logsDao.logMessage(
            Type.MESSAGE,
            channel,
            JavabotUser("ChattyCathy", "ChattyCathy", "localhost"),
            "test message",
        )

        Assert.assertNotNull(logsDao.getSeen(channel.name, "chattycathy"))
        Assert.assertFalse(
            logsDao.findByChannel(channel.name, LocalDateTime.now(), false).isEmpty()
        )
        Assert.assertTrue(
            logsDao.findByChannel(channel.name, LocalDateTime.now().minusDays(1), false).isEmpty()
        )
    }

    @Test
    fun channelEvents() {
        val chanName = "##testChannel"
        channelDao.delete(chanName)
        val channel = channelDao.create(chanName, true, null)
        logsDao.deleteAllForChannel(chanName)

        logsDao.logMessage(
            PART,
            channel,
            TEST_USER,
            Sofia.userParted(TEST_USER.nick, "i'm out of here!"),
        )

        val logs = logsDao.findByChannel(chanName, LocalDateTime.now(), true)

        Assert.assertFalse(logs.isEmpty(), "Should have one log entry")
        Assert.assertEquals(logs[0].message, Sofia.userParted(TEST_USER.nick, "i'm out of here!"))
    }
}
