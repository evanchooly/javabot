package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message

public class DisableOperation : OperationsCommand() {
    @Param
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