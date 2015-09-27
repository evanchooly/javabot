package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message

public class EnableOperation : OperationsCommand() {
    @Param
    lateinit var operationName: String

    override fun execute(event: Message) {
        bot.enableOperation(operationName)
        bot.postMessageToChannel(event, Sofia.adminOperationEnabled(operationName))
        listCurrent(event)
    }
}
