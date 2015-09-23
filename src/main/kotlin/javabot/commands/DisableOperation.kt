package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ConfigDao

import javax.inject.Inject

public class DisableOperation : OperationsCommand() {
    Param
    var name: String

    override fun execute(event: Message) {
        if (bot.disableOperation(name)) {
            bot.postMessageToChannel(event, Sofia.adminOperationDisabled(name))
            listCurrent(event)
        } else {
            bot.postMessageToChannel(event, Sofia.adminOperationNotDisabled(name))
        }
    }
}