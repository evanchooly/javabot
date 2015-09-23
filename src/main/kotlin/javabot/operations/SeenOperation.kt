package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.Seen
import javabot.dao.LogsDao
import org.pircbotx.Channel

import javax.inject.Inject
import java.time.format.DateTimeFormatter

public class SeenOperation : BotOperation() {
    Inject
    private val dao: LogsDao? = null

    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        val channel = event.channel
        if (channel != null && "seen ".equalsIgnoreCase(message.substring(0, Math.min(message.length(), 5)))) {
            val key = message.substring("seen ".length())
            val seen = dao!!.getSeen(channel.name, key)
            if (seen != null) {
                bot.postMessageToChannel(event,
                      Sofia.seenLast(event.user.nick, key, seen.updated.format(FORMATTER),
                            seen.message))
            } else {
                bot.postMessageToChannel(event, Sofia.seenUnknown(event.user.nick, key))
            }
            return true
        }
        return false
    }

    companion object {
        public val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    }
}