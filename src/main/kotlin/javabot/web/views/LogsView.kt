package javabot.web.views

import com.antwerkz.sofia.Sofia
import com.google.inject.assistedinject.Assisted
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javabot.dao.LogsDao
import javabot.dao.util.CleanHtmlConverter
import javabot.web.resources.BotResource
import javabot.model.Logs
import javabot.web.resources.BotResource.Companion
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

class LogsView @Inject constructor(
        adminDao: AdminDao,
        channelDao: ChannelDao,
        factoidDao: FactoidDao,
        apiDao: ApiDao,
        var logsDao: LogsDao,
        @Assisted request: HttpServletRequest,
        @Assisted val channel: String,
        @Assisted private val date: LocalDateTime) :
        MainView(adminDao, channelDao, factoidDao, apiDao, request) {
    companion object {
        val LOG_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm")
    }

    val today = BotResource.FORMAT.format(date)
    val yesterday = BotResource.FORMAT.format(date.minusDays(1))
    val tomorrow = BotResource.FORMAT.format(date.plusDays(1))

    override fun format(date: LocalDateTime?): String {
        return if (date != null) LOG_FORMAT.format(date) else ""
    }

    fun logs(): List<Logs> {
        val logs = logsDao.findByChannel(channel, date, isAdmin())
        // filter the log content
        for (log in logs) {
            log.message = CleanHtmlConverter.convert(log.message) { s -> Sofia.logsAnchorFormat(s, s) }
        }
        return logs
    }

    override fun getChildView(): String {
        return "logs.ftl"
    }

}
