package javabot.operations

import com.antwerkz.sofia.Sofia
import dev.morphia.Datastore
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import dev.morphia.query.filters.Filters.eq
import java.time.format.DateTimeFormatter.ofPattern
import java.util.Locale
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.model.Logs
import javax.inject.Inject

class LogsOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var ds: Datastore) :
    BotOperation(bot, adminDao) {

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if (message.lowercase(Locale.getDefault()).startsWith(KEYWORD_LOGS)) {
            val nickname = message.substring(KEYWORD_LOGS.length).trim()
            val query = ds.find(Logs::class.java)
            query.filter(eq("channel", event.channel!!.name))
            val options = FindOptions().sort(Sort.ascending("updated"))

            if (nickname.isEmpty()) {
                options.limit(200)
            } else {
                query.filter(eq("nick", nickname))
                options.limit(50)
            }
            query.forEach {
                responses.add(
                    Message(
                        event,
                        Sofia.logsEntry(
                            it.updated.format(ofPattern("HH:mm")),
                            it.nick!!,
                            it.message,
                        ),
                    )
                )
            }
            if (responses.isEmpty()) {
                responses.add(
                    Message(
                        event,
                        if (nickname.isEmpty()) Sofia.logsNone()
                        else Sofia.logsNoneForNick(nickname),
                    )
                )
            }
        }
        return responses
    }

    companion object {
        private val KEYWORD_LOGS = "logs"
    }
}
