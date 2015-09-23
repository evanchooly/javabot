package javabot.kotlin.web.views

import com.antwerkz.sofia.Sofia
import com.google.inject.Injector
import javabot.dao.LogsDao
import javabot.dao.util.CleanHtmlConverter
import javabot.kotlin.web.resources.BotResource
import javabot.model.Logs

import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

public class LogsView(injector: Injector, request: HttpServletRequest, public val channel: String, private val date: LocalDateTime) : MainView(
      injector, request) {
    @Inject
    private val logsDao: LogsDao? = null
    public val today: String
    public val yesterday: String
    public val tomorrow: String

    init {
        today = BotResource.FORMAT.format(date)
        yesterday = BotResource.FORMAT.format(date.minusDays(1))
        tomorrow = BotResource.FORMAT.format(date.plusDays(1))
    }

    override fun format(date: LocalDateTime): String {
        return LOG_FORMAT.format(date)
    }

    public fun logs(): List<Logs> {
        val logs = logsDao!!.findByChannel(channel, date, isAdmin())
        // filter the log content
        for (log in logs) {
            log.message = CleanHtmlConverter.convert(log.message) { s -> Sofia.logsAnchorFormat(s, s) }
        }
        return logs
    }

    override fun getChildView(): String {
        return "logs.ftl"
    }

    companion object {
        public val LOG_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm")
    }

}
