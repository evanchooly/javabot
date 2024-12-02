package javabot.operations

import com.antwerkz.sofia.Sofia
import jakarta.inject.Inject
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ConfigDao
import javabot.model.NickRegistration

class RegisterNickOperation
@Inject
constructor(bot: Javabot, adminDao: AdminDao, var configDao: ConfigDao) :
    BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if (message.startsWith("register ")) {
            val split = message.split(" ")
            if (split.size > 1) {
                val twitterName = split[1]
                val registration = NickRegistration(event.user, twitterName)
                adminDao.save(registration)

                responses.add(
                    Message(
                        event.user,
                        Sofia.registerNick(configDao.get().url, registration.url ?: "", twitterName)
                    )
                )
            }
        }
        return responses
    }
}
