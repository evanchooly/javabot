package javabot.operations

import com.antwerkz.sofia.Sofia
import dev.morphia.Datastore
import dev.morphia.query.FindOptions
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.model.criteria.LogsCriteria
import java.time.format.DateTimeFormatter.ofPattern
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
            val options = FindOptions()
            if (nickname.isEmpty()) {
                options.limit(200)
            } else {
                criteria.nick(nickname)
                options.limit(50)
            }
            criteria.query().find(options).forEach {
                responses.add(Message(event, Sofia.logsEntry(it.updated.format(ofPattern("HH:mm")), it.nick!!, it.message)))
            }
            if (responses.isEmpty()) {
                responses.add(Message(event, if (nickname.isEmpty()) Sofia.logsNone() else Sofia.logsNoneForNick(nickname)))
            }
        }
        return responses
    }

    companion object {
        private const val KEYWORD_LOGS = "logs"
    }
}
