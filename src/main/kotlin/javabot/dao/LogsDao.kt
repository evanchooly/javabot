package javabot.dao

import com.google.inject.Inject
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale
import javabot.Seen
import javabot.model.Channel
import javabot.model.JavabotUser
import javabot.model.Logs
import javabot.model.Logs.Type
import javabot.model.criteria.LogsCriteria.Companion.channel
import javabot.model.criteria.LogsCriteria.Companion.updated
import javabot.model.criteria.LogsCriteria.Companion.upperNick
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
        val criteria =
            ds.find(Logs::class.java)
                .filter(upperNick().eq(nick.uppercase(Locale.getDefault())), channel().eq(channel))
        val first = criteria.first(FindOptions().sort(Sort.descending(updated)))

        return first?.let { Seen(it.channel!!, it.message, it.nick, it.updated) }
    }

    private fun dailyLog(channelName: String, date: LocalDateTime?, logged: Boolean): List<Logs> {
        var list: List<Logs> = listOf()
        if (logged) {
            val start = if (date == null) LocalDate.now() else date.toLocalDate()
            val tomorrow = start.plusDays(1)
            val nextMidnight = tomorrow.atStartOfDay()
            val lastMidnight = start.atStartOfDay()
            val criteria =
                ds.find(Logs::class.java)
                    .filter(
                        channel().eq(channelName),
                        updated().lte(nextMidnight),
                        updated().gte(lastMidnight)
                    )
            list = criteria.iterator(FindOptions().sort(Sort.ascending(updated))).toList()
        }
        return list
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
        ds.find(Logs::class.java).filter(channel().eq(channel)).delete(DeleteOptions().multi(true))
    }
}
