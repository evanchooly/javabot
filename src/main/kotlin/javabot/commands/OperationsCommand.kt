package javabot.commands

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import java.lang.String.join
import java.util.ArrayList

abstract class OperationsCommand @Inject constructor(bot: Javabot, adminDao: AdminDao) : AdminCommand(bot, adminDao) {
    protected fun listCurrent(event: Message, responses: ArrayList<Message>) {
        responses.add(Message(event, Sofia.adminRunningOperations(event.user.nick)))
        responses.add(Message(event, join(",", bot.activeOperations.map({ it.getName() }))))
    }
}