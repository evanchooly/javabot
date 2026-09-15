package javabot.qtest.dao

import com.antwerkz.sofia.Sofia
import dev.morphia.Datastore
import dev.morphia.query.filters.Filters
import io.quarkus.test.junit.QuarkusTest
import java.time.LocalDateTime
import javabot.BaseTest
import javabot.model.Channel
import javabot.model.JavabotUser
import javabot.model.Logs
import javabot.model.Logs.Type
import javabot.model.Logs.Type.PART
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@QuarkusTest
class LogsDaoTest : BaseTest() {
    private val ds: Datastore by lazy { injector.getInstance(Datastore::class.java) }

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

        assertNotNull(logsDao.getSeen(channel.name, "chattycathy"))
        assertFalse(logsDao.findByChannel(channel.name, LocalDateTime.now(), false).isEmpty())
        assertTrue(
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

        assertFalse(logs.isEmpty(), "Should have one log entry")
        assertEquals(Sofia.userParted(TEST_USER.nick, "i'm out of here!"), logs[0].message)
    }
}
