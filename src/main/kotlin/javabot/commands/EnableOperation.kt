package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import com.google.inject.Inject
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao

class EnableOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : OperationsCommand(bot, adminDao) {
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
