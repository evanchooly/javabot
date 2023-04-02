package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.FactoidDao
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/**
 * Simple operation to pull who added the factoid and when it was added
 */
class InfoOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var factoidDao: FactoidDao) :
        BotOperation(bot, adminDao)  {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value.lowercase(Locale.getDefault())
        if (message.startsWith("info ")) {
            val key = message.substring("info ".length)
            val factoid = factoidDao.getFactoid(key)
            if (factoid != null) {
                val formatter = DateTimeFormatter.ofPattern(INFO_DATE_FORMAT)
                val updated = factoid.updated
                val formatted = formatter.format(updated)
                responses.add(
                    Message(
                        event, Sofia.factoidInfo(
                            key, if (factoid.locked) "*" else "", factoid.userName,
                            formatted, factoid.value
                        )
                    )
                )
            } else {
                responses.add(Message(event, Sofia.factoidUnknown(key)))
            }
        }
        return responses
    }

    companion object {
        val INFO_DATE_FORMAT: String = "dd MMM yyyy' at 'KK:mm"
    }

}