package javabot.operations

import com.google.inject.Inject
import java.util.Calendar
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao

class TimeOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) :
    BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if ("time" == message || "date" == message) {
            responses.add(Message(event, Calendar.getInstance().time.toString()))
        }
        return responses
    }
}
