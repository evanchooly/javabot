package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.operations.BotOperation

import java.util.stream.Collectors

import java.lang.String.join

public abstract class OperationsCommand : AdminCommand() {
    protected fun listCurrent(event: Message) {
        bot.postMessageToChannel(event, Sofia.adminRunningOperations(event.user.nick))
        bot.postMessageToChannel(event, join(",", bot.activeOperations.map( { it.getName() })))
    }
}