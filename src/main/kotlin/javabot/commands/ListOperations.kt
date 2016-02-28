package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ConfigDao
import javabot.operations.BotOperation
import org.apache.commons.lang.StringUtils
import org.pircbotx.PircBotX

import javax.inject.Inject
import javax.inject.Provider

class ListOperations @Inject constructor(bot: Javabot, adminDao: AdminDao, ircBot: com.google.inject.Provider<PircBotX>, var configDao: ConfigDao) :
        OperationsCommand(bot, adminDao, ircBot) {

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        responses.add(Message(event, Sofia.adminKnownOperations(event.user.nick,
                StringUtils.join(configDao.list(BotOperation::class.java).iterator(), ","))))

        listCurrent(event, responses)
        responses.add(Message(event, Sofia.adminOperationInstructions()))
        return responses
    }
}