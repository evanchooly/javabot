package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.model.criteria.LogsCriteria
import org.mongodb.morphia.Datastore
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class LogsOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var ds: Datastore) : BotOperation(bot, adminDao) {

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if (message.toLowerCase().startsWith(KEYWORD_LOGS)) {
            val nickname = message.substring(KEYWORD_LOGS.length).trim()
            val criteria = LogsCriteria(ds)
            criteria.channel(event.channel!!.name)
            criteria.updated().order(true)
            if (nickname.isEmpty()) {
                criteria.query().limit(200)
            } else {
                criteria.nick(nickname)
                criteria.query().limit(50)
            }
            criteria.query().fetch().mapTo(responses) {
                Message(event, Sofia.logsEntry(it.updated.format(DateTimeFormatter.ofPattern("HH:mm")), it.nick!!, it.message))
            }
            if (responses.isEmpty()) {
                responses.add(Message(event, if (nickname.isEmpty()) Sofia.logsNone() else Sofia.logsNoneForNick(nickname)))
            }
        }
        return responses
    }

    companion object {
        private val KEYWORD_LOGS = "logs"
    }
}
