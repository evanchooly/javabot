package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.commands.AdminCommand

import java.util.ArrayList
import java.util.ServiceLoader
import java.util.stream.Collectors

public class ListAdminCommands : AdminCommand() {
    override fun execute(event: Message) {
        val list = listCommands().stream().map(Function<AdminCommand, String> { it.getCommandName() }).collect(Collectors.toList<String>())
        bot.postMessageToChannel(event, Sofia.adminKnownCommands(event.user.nick, String.join(", ", list)))
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