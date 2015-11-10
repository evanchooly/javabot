package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

public class DaysUntilOperation : BotOperation() {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        var message = event.value.toLowerCase()
        if (message.startsWith("days until ")) {
            val sender = event.user
            message = message.substring("days until ".length)
            var d: LocalDateTime? = null
            val formats = arrayOf(DateTimeFormatter.ofPattern("yyyy/MM/dd"), DateTimeFormatter.ofPattern("MMM d, ''yy"),
                  DateTimeFormatter.ofPattern("d MMM yyyy"), DateTimeFormatter.ofPattern("MMM d, yyyy"),
                  DateTimeFormatter.ofPattern("MMM d, ''yy"))
            var i = 0
            while (i < formats.size && d == null) {
                try {
                    d = LocalDateTime.parse(message, formats[i])
                    calcTime(responses, event, d)
                } catch (e: IllegalArgumentException) {
                    // I think we just want to ignore this...
                }

                i++
            }
            if (d == null) {
                responses.add(Message(event, Sofia.invalidDateFormat(sender.nick)))
            }
        }
        return responses
    }

    private fun calcTime(responses: MutableList<Message>, event: Message, d: LocalDateTime) {
        val days = Duration.between(d, LocalDateTime.now().withHour(0)).toDays()
        val l = d.toLocalDate().toEpochDay()
        responses.add(Message(event, Sofia.daysUntil(event.user.nick, days, Date(l))))
    }
}
