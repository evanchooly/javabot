package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ShunDao
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import javax.inject.Inject

/**
 * Causes the bot to disregard bot triggers for a few minutes. Useful to de-fang abusive users without ejecting the bot from a channel
 * entirely.
 */
class ShunOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var shunDao: ShunDao) : BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if (message.startsWith("shun ")) {
            val parts = message.substring(5).split(" ")
            if (parts.size == 0) {
                responses.add(Message(event, Sofia.shunUsage()))
            } else {
                responses.add(Message(event, getShunnedMessage(parts)))
            }
        }
        return responses
    }

    private fun getShunnedMessage(parts: List<String>): String {
        val victim = parts[0]
        if (shunDao.isShunned(victim)) {
            return Sofia.alreadyShunned(victim)
        }
        val until = if (parts.size == 1)
            LocalDateTime.now().plusMinutes(5)
        else
            LocalDateTime.now().plusSeconds(Integer.parseInt(parts[1]).toLong())
        shunDao.addShun(victim, until)

        return Sofia.shunned(victim, Date(until.toEpochSecond(ZoneOffset.UTC)))
    }
}