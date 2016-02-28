package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.FactoidDao
import java.time.Duration
import java.time.Instant.now
import javax.inject.Inject

class StatsOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var factoidDao: FactoidDao) : BotOperation(bot, adminDao) {
    private var numberOfMessages = 0

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        numberOfMessages++
        val message = event.value
        if ("stats".equals(message, ignoreCase = true)) {
            responses.add(Message(event, Sofia.botStats(Duration.between(startTime, now()).toDays(), numberOfMessages, factoidDao.count())))
        }
        return responses
    }

    companion object {
        private val startTime = now()
    }
}