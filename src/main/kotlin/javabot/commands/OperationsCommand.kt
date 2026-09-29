package javabot.commands

import com.antwerkz.sofia.Sofia
import jakarta.inject.Inject
import java.lang.String.join
import java.util.ArrayList
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao

abstract class OperationsCommand @Inject constructor(bot: Javabot, adminDao: AdminDao) :
    AdminCommand(bot, adminDao) {
    protected fun listCurrent(event: Message, responses: ArrayList<Message>) {
        responses.add(Message(event, Sofia.adminRunningOperations(event.user.nick)))
        responses.add(Message(event, join(",", bot.activeOperations.map({ it.getName() }))))
    }
}
