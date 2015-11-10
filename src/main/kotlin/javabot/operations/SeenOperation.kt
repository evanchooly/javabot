package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.LogsDao
import java.time.format.DateTimeFormatter
import javax.inject.Inject

public class SeenOperation : BotOperation() {
    @Inject
    lateinit var dao: LogsDao

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        val channel = event.channel
        if (channel != null && "seen ".equals(message.substring(0, Math.min(message.length, 5)), ignoreCase = true)) {
            val key = message.substring("seen ".length)
            val seen = dao.getSeen(channel.name, key)
            if (seen != null) {
                responses.add(Message(event, Sofia.seenLast(event.user.nick, key, seen.updated.format(FORMATTER), seen.message)))
            } else {
                responses.add(Message(event, Sofia.seenUnknown(event.user.nick, key)))
            }
        }
        return responses
    }

    companion object {
        public val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    }
}