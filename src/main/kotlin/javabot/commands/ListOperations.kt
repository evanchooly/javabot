package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ConfigDao
import javabot.operations.BotOperation
import org.apache.commons.lang.StringUtils

import javax.inject.Inject

public class ListOperations : OperationsCommand() {
    Inject
    private val configDao: ConfigDao? = null

    override fun execute(event: Message) {
        bot.postMessageToChannel(event, Sofia.adminKnownOperations(event.user.nick,
              StringUtils.join(configDao!!.list(BotOperation::class.java).iterator(), ",")))

        listCurrent(event)
        bot.postMessageToChannel(event, Sofia.adminOperationInstructions())
    }
}