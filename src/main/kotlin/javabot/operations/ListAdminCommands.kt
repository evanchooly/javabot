package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.commands.AdminCommand
import org.pircbotx.PircBotX
import java.util.ServiceLoader
import javax.inject.Inject
import javax.inject.Provider

class ListAdminCommands @Inject constructor(javabot: Provider<Javabot>, pircBot: Provider<PircBotX>):
        AdminCommand(javabot, pircBot) {
    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val list = listCommands().map({ it.getCommandName() })
        responses.add(Message(event, Sofia.adminKnownCommands(event.user.nick, list.joinToString(", "))))
        return responses
    }

    fun listCommands(): List<AdminCommand> {
        val loader = ServiceLoader.load(AdminCommand::class.java)
        val list = arrayListOf<AdminCommand>()
        list.addAll(loader)
        return list
    }
}