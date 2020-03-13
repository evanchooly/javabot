package javabot.operations

import com.antwerkz.sofia.Sofia
import dev.morphia.Datastore
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import dev.morphia.query.experimental.filters.Filters.eq
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.model.Logs
import java.time.format.DateTimeFormatter.ofPattern
import javax.inject.Inject

class LogsOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var ds: Datastore) : BotOperation(bot, adminDao) {

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if (message.toLowerCase().startsWith(KEYWORD_LOGS)) {
            val nickname = message.substring(KEYWORD_LOGS.length).trim()
            val query = ds.find(Logs::class.java)
            query.filter(eq("channel", event.channel!!.name))
            val options = FindOptions()
                    .sort(Sort.ascending("updated"))

            if (nickname.isEmpty()) {
                options.limit(200)
            } else {
                query.filter(eq("nick", nickname))
                options.limit(50)
            }
            query.execute(FindOptions()).forEach {
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
