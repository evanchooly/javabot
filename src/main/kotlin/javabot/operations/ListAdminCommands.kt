package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.commands.AdminCommand

import java.util.ArrayList
import java.util.ServiceLoader
import java.util.stream.Collectors

public class ListAdminCommands : AdminCommand() {
    override fun execute(event: Message) {
        val list = listCommands().map({ it.getCommandName() })
        bot.postMessageToChannel(event, Sofia.adminKnownCommands(event.user.nick, list.join(", ")))
    }

    public fun listCommands(): List<AdminCommand> {
        val loader = ServiceLoader.load(AdminCommand::class.java)
        val list = ArrayList<AdminCommand>()
        for (command in loader) {
            list.add(command)
        }
        return list
    }
}