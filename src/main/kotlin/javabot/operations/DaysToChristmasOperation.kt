package javabot.operations

import com.antwerkz.sofia.Sofia
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.Month
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javax.inject.Inject

class DaysToChristmasOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) :
    BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        if ("countdown to christmas" == event.value.lowercase(Locale.getDefault())) {
            val christmas = LocalDateTime.of(now().year, Month.DECEMBER, 25, 0, 0, 0)
            val now = now()
            val duration = Duration.between(now, christmas)
            val cal = Calendar.getInstance()
            cal.set(Calendar.MONTH, 12)
            cal.set(Calendar.DAY_OF_MONTH, 25)
            val instant = christmas.atZone(ZoneId.systemDefault()).toInstant()

            responses.add(
                Message(
                    event,
                    Sofia.daysUntil(event.user.nick, duration.toDays(), Date.from(instant)),
                )
            )
        }
        return responses
    }
}
