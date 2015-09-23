package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

import java.time.LocalDateTime.now

public class DaysToChristmasOperation : BotOperation() {
    override fun handleMessage(event: Message): Boolean {
        if ("countdown to christmas" == event.value.toLowerCase()) {
            val christmas = LocalDateTime.of(now().year, Month.DECEMBER, 25, 0, 0, 0)
            val now = now()
            val duration = Duration.between(now, christmas)
            val cal = Calendar.getInstance()
            cal.set(Calendar.MONTH, 12)
            cal.set(Calendar.DAY_OF_MONTH, 25)
            val instant = christmas.atZone(ZoneId.systemDefault()).toInstant()

            bot.postMessageToChannel(event, Sofia.daysUntil(event.user.nick, duration.toDays(), Date.from(instant)))
            return true
        }
        return false
    }
}
