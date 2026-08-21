package javabot.operations

import com.antwerkz.sofia.Sofia
import jakarta.inject.Inject
import java.util.Locale
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.FactoidDao

class LiteralOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var dao: FactoidDao) :
    BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value.lowercase(Locale.getDefault())
        if (message.startsWith("literal ")) {
            val key = message.substring("literal ".length)
            val factoid = dao.getFactoid(key)
            responses.add(
                Message(event, if (factoid != null) factoid.value else Sofia.factoidUnknown(key))
            )
        }
        return responses
    }
}
