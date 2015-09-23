package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ConfigDao

import javax.inject.Inject

public class EnableOperation : OperationsCommand() {
    Param
    var name: String

    override fun execute(event: Message) {
        bot.enableOperation(name)
        bot.postMessageToChannel(event, Sofia.adminOperationEnabled(name))
        listCurrent(event)
    }
}
