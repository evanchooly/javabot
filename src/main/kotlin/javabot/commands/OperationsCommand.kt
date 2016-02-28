package javabot.commands

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import javabot.Javabot
import javabot.Message
import org.pircbotx.PircBotX
import java.lang.String.join
import java.util.ArrayList
import javax.inject.Provider

abstract class OperationsCommand @Inject constructor(javabot: Provider<Javabot>, ircBot: Provider<PircBotX>) :
        AdminCommand(javabot, ircBot) {
    protected fun listCurrent(event: Message, responses: ArrayList<Message>) {
        responses.add(Message(event, Sofia.adminRunningOperations(event.user.nick)))
        responses.add(Message(event, join(",", bot.activeOperations.map({ it.getName() }))))
    }
}