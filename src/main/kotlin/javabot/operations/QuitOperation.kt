package javabot.operations

import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ConfigDao

import javax.inject.Inject

class QuitOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var configDao: ConfigDao) : BotOperation(bot, adminDao) {

    override fun handleMessage(event: Message): List<Message> {
        val message = event.value
        if (message.toLowerCase().startsWith("quit ")) {
            if (message.substring("quit ".length) == configDao.get().password) {
                System.exit(0)
            }
        }
        return listOf()
    }
}