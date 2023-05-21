package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao

class IgnoreOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) :
    BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if (message.startsWith("ignore ")) {
            val parts = message.split(" ")
            bot.addIgnore(parts[1])
            responses.add(Message(event, Sofia.botIgnoring(parts[1])))
        }
        return responses
    }
}
