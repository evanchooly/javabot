package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import com.google.inject.Inject
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao

class DisableOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : OperationsCommand(bot, adminDao) {
    @Parameter(names=arrayOf("-name"), description = "The name of the operation to disable", required = true)
    lateinit var operationName: String

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        if (bot.disableOperation(operationName)) {
            responses.add(Message(event, Sofia.adminOperationDisabled(operationName)))
            listCurrent(event, responses)
        } else {
            responses.add(Message(event, Sofia.adminOperationNotDisabled(operationName)))
        }

        return responses
    }
}