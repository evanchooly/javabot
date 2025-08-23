package javabot.dao

import com.google.inject.Inject
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import dev.morphia.query.filters.Filters.eq
import dev.morphia.query.filters.Filters.gte
import dev.morphia.query.filters.Filters.lte
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale
import javabot.Seen
import javabot.model.Channel
import javabot.model.JavabotUser
import javabot.model.Logs
import javabot.model.Logs.Type
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogsDao @Inject constructor(ds: Datastore, var dao: ConfigDao, var channelDao: ChannelDao) :
    BaseDao<Logs>(ds, Logs::class.java) {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(LogsDao::class.java)
    }

    fun logMessage(type: Type, channel: Channel?, user: JavabotUser?, message: String) {
        save(Logs(user?.nick, message, type, channel?.name))
    }

    fun isSeen(channel: String, nick: String): Boolean {
        return getSeen(channel, nick) != null
    }

    fun getSeen(channel: String, nick: String): Seen? {
        val first =
            ds.find(Logs::class.java)
                .filter(
                    eq("upperNick", nick.uppercase(Locale.getDefault())),
                    eq("channel", channel),
                )
                .first(FindOptions().sort(Sort.descending("updated")))

        return first?.let { Seen(it.channel!!, it.message, it.nick, it.updated) }
    }

    private fun dailyLog(channelName: String, date: LocalDateTime?, logged: Boolean) =
        if (logged) listOf()
        else {
            val start = if (date == null) LocalDate.now() else date.toLocalDate()
            val tomorrow = start.plusDays(1)
            val nextMidnight = tomorrow.atStartOfDay()
            val lastMidnight = start.atStartOfDay()
            ds.find<Logs>(Logs::class.java)
                .filter(
                    eq("channel", channelName),
                    lte("updated", nextMidnight),
                    gte("updated", lastMidnight),
                )
                .iterator(FindOptions().sort(Sort.ascending("updated")))
                .toList()
        }

    fun findByChannel(name: String, date: LocalDateTime, showAll: Boolean): List<Logs> {
        val channel = channelDao.get(name)
        if (channel != null && (showAll || channel.logged)) {
            return dailyLog(name, date, showAll || channel.logged)
        } else {
            return emptyList()
        }
    }

    fun deleteAllForChannel(channel: String) {
        ds.find(Logs::class.java).filter(eq("channel", channel)).delete(DeleteOptions().multi(true))
    }
}
