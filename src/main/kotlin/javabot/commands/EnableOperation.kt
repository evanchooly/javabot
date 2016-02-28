package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import com.google.inject.Inject
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import org.pircbotx.PircBotX
import javax.inject.Provider

class EnableOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, ircBot: com.google.inject.Provider<PircBotX>) :
        OperationsCommand(bot, adminDao, ircBot) {
    @Parameter(names = arrayOf("--name"))
    lateinit var operationName: String

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        bot.enableOperation(operationName)
        responses.add(Message(event, Sofia.adminOperationEnabled(operationName)))
        listCurrent(event, responses)
        
        return responses
    }
}
