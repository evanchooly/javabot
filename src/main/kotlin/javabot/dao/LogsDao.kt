package javabot.dao

import com.google.inject.Inject
import javabot.Seen
import javabot.model.Channel
import javabot.model.JavabotUser
import javabot.model.Logs
import javabot.model.Logs.Type
import javabot.model.criteria.LogsCriteria
import org.mongodb.morphia.Datastore
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime

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
        val criteria = LogsCriteria(ds)
        criteria.upperNick().equal(nick.toUpperCase())
        criteria.channel().equal(channel)
        criteria.updated().order(false)
        val logs = criteria.query().get()
        return if (logs != null) Seen(logs.channel!!, logs.message, logs.nick, logs.updated) else null
    }

    private fun dailyLog(channelName: String, date: LocalDateTime?, logged: Boolean): List<Logs> {
        var list: List<Logs> = listOf()
        if (logged) {
            val start = if (date == null) LocalDate.now() else date.toLocalDate()
            val tomorrow = start.plusDays(1)
            val criteria = LogsCriteria(ds)
            criteria.channel(channelName)
            val nextMidnight = tomorrow.atStartOfDay()
            val lastMidnight = start.atStartOfDay()
            criteria.and(
                  criteria.updated().lessThanOrEq(nextMidnight),
                  criteria.updated().greaterThanOrEq(lastMidnight))
            list = criteria.query().order("updated").asList()
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
        val criteria = LogsCriteria(ds)
        criteria.channel(channel)
        ds.delete(criteria.query())
    }
}