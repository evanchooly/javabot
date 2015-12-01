package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Message

public class DisableOperation : OperationsCommand() {
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