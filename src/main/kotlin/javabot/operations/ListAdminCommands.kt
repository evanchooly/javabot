package javabot.operations

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import com.google.inject.Provider
import javabot.Javabot
import javabot.Message
import javabot.commands.AdminCommand
import javabot.dao.AdminDao
import org.pircbotx.PircBotX
import java.util.ServiceLoader

class ListAdminCommands @Inject constructor(bot: Javabot, adminDao: AdminDao, ircBot: Provider<PircBotX>):
        AdminCommand(bot, adminDao, ircBot) {
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