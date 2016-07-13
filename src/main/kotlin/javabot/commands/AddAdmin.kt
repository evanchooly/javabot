package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javax.inject.Inject

class AddAdmin @Inject constructor(bot: Javabot, adminDao: AdminDao) :
        AdminCommand(bot, adminDao) {
    @Parameter(required = true)
    lateinit var userName: String
    @Parameter(required = true)
    lateinit var hostName: String

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val user = event.user
        if (adminDao.getAdmin(user.nick, hostName) != null) {
            responses.add(Message(event, Sofia.adminAlready(user.nick)))
        } else {
            adminDao.create(user.nick, user.userName, user.hostmask)
            responses.add(Message(event, Sofia.adminAdded(user.nick)))
        }

        return responses
    }
}
