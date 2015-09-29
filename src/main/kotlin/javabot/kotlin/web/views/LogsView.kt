package javabot.kotlin.web.views

import com.antwerkz.sofia.Sofia
import com.google.inject.Injector
import javabot.dao.LogsDao
import javabot.dao.util.CleanHtmlConverter
import javabot.model.Logs
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

public class LogsView(injector: Injector, request: HttpServletRequest,
                      val channel: String,
                      private val date: LocalDateTime) : MainView(
      injector, request) {
    companion object {
        public val LOG_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm")
    }

    @Inject
    lateinit var logsDao: LogsDao
    val today = javabot.kotlin.web.resources.BotResource.Companion.FORMAT.format(date)
    val yesterday = javabot.kotlin.web.resources.BotResource.Companion.FORMAT.format(date.minusDays(1))
    val tomorrow = javabot.kotlin.web.resources.BotResource.Companion.FORMAT.format(date.plusDays(1))

    init {
    }

    override fun format(date: LocalDateTime): String {
        return LOG_FORMAT.format(date)
    }

    public fun logs(): List<Logs> {
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
