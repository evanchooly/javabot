package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.commands.AdminCommand
import java.util.ServiceLoader

public class ListAdminCommands : AdminCommand() {
    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val list = listCommands().map({ it.getCommandName() })
        responses.add(Message(event, Sofia.adminKnownCommands(event.user.nick, list.joinToString(", "))))
        return responses
    }

    public fun listCommands(): List<AdminCommand> {
        val loader = ServiceLoader.load(AdminCommand::class.java)
        val list = arrayListOf<AdminCommand>()
        list.addAll(loader)
        return list
    }
}