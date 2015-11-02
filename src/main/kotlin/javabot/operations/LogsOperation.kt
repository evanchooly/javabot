package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.model.criteria.LogsCriteria
import org.mongodb.morphia.Datastore
import java.time.format.DateTimeFormatter
import javax.inject.Inject

public class LogsOperation : BotOperation() {

    @Inject
    lateinit var ds: Datastore

    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        if (message.toLowerCase().startsWith(KEYWORD_LOGS)) {
            val nickname = message.substring(KEYWORD_LOGS.length).trim()
            val criteria = LogsCriteria(ds)
            criteria.channel(event.channel!!.name)
            criteria.updated().order(true)
            var handled = false
            if (nickname.isEmpty()) {
                criteria.query().limit(200)
            } else {
                criteria.nick(nickname)
                criteria.query().limit(50)
            }
            for (logs in criteria.query().fetch()) {
                bot.postMessageToChannel(event, Sofia.logsEntry(logs.updated.format(DateTimeFormatter.ofPattern("HH:mm")),
                      logs.nick, logs.message))
                handled = true
            }
            if (!handled) {
                bot.postMessageToChannel(event, if (nickname.isEmpty()) Sofia.logsNone() else Sofia.logsNoneForNick(nickname))
            }
            return true
        }
        return false
    }

    companion object {
        private val KEYWORD_LOGS = "logs"
    }
}
