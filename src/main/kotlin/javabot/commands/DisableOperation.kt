package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Message

public class DisableOperation : OperationsCommand() {
    @Parameter(names=arrayOf("-name"), description = "The name of the operation to disable", required = true)
    lateinit var operationName: String

    override fun execute(event: Message) {
        if (bot.disableOperation(operationName)) {
            bot.postMessageToChannel(event, Sofia.adminOperationDisabled(operationName))
            listCurrent(event)
        } else {
            bot.postMessageToChannel(event, Sofia.adminOperationNotDisabled(operationName))
        }
    }
}