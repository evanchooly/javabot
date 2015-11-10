package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.operations.BotOperation

import java.util.stream.Collectors

import java.lang.String.join
import java.util.ArrayList

public abstract class OperationsCommand : AdminCommand() {
    protected fun listCurrent(event: Message, responses: ArrayList<Message>) {
        responses.add(Message(event, Sofia.adminRunningOperations(event.user.nick)))
        responses.add(Message(event, join(",", bot.activeOperations.map( { it.getName() }))))
    }
}