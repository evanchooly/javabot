package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import com.google.inject.Inject
import javabot.Javabot
import javabot.Message
import org.pircbotx.PircBotX
import javax.inject.Provider

class DisableOperation @Inject constructor(javabot: Provider<Javabot>, ircBot: Provider<PircBotX>) :
        OperationsCommand(javabot, ircBot) {
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