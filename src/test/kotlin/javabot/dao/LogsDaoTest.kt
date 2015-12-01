package javabot.dao

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.model.Channel
import javabot.model.Logs
import javabot.model.Logs.Type
import javabot.model.criteria.LogsCriteria
import org.mongodb.morphia.Datastore
import org.testng.Assert
import org.testng.annotations.Test
import java.time.LocalDateTime
import javax.inject.Inject

public class LogsDaoTest : BaseTest() {
    @Inject
    protected lateinit var ds: Datastore

    @Test
    public fun seen() {
        val logsCriteria = LogsCriteria(ds)
        logsCriteria.channel(CHANNEL_NAME)
        logsCriteria.delete()
        channelDao.delete(channelDao.get(CHANNEL_NAME))
        val channel = Channel()
        channel.name = CHANNEL_NAME
        channel.logged = true
        channelDao.save(channel)
        logsDao.logMessage(Type.MESSAGE, ircBot.get().userChannelDao.getChannel(channel.name),
              userFactory.createUser("ChattyCathy", "ChattyCathy", "localhost"), "test message")

        Assert.assertNotNull(logsDao.getSeen(channel.name, "chattycathy"))
        Assert.assertFalse(logsDao.findByChannel(channel.name, LocalDateTime.now(), false).isEmpty())
        Assert.assertTrue(logsDao.findByChannel(channel.name, LocalDateTime.now().minusDays(1), false).isEmpty())
    }

    @Test
    public fun channelEvents() {
        val chanName = "##testChannel"
        channelDao.delete(chanName)
        channelDao.create(chanName, true, null)
        logsDao.deleteAllForChannel(chanName)

        val bot = ircBot.get()
        val channel = bot.userChannelDao.getChannel(chanName)
        val user = bot.userChannelDao.getUser(testUser.nick)

        logsDao.logMessage(Logs.Type.PART, channel, user, Sofia.userParted(user.nick, "i'm out of here!"))

        val logs = logsDao.findByChannel(chanName, LocalDateTime.now(), true)

        Assert.assertFalse(logs.isEmpty(), "Should have one log entry")
        Assert.assertEquals(logs[0].message, Sofia.userParted(user.nick, "i'm out of here!"))
    }

    companion object {

        public val CHANNEL_NAME: String = "#watercooler"
    }
}
