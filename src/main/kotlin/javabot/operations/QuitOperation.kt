package javabot.operations

import javabot.Message
import javabot.dao.ConfigDao

import javax.inject.Inject

public class QuitOperation : BotOperation() {

    @Inject
    lateinit var configDao: ConfigDao

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