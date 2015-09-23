package javabot.commands

import com.antwerkz.sofia.Sofia
import com.google.inject.Provider
import javabot.Message
import javabot.dao.AdminDao
import org.pircbotx.PircBotX
import org.pircbotx.User

import javax.inject.Inject

public class AddAdmin : AdminCommand() {
    Inject
    private val dao: AdminDao? = null
    Inject
    private val ircBot: Provider<PircBotX>? = null
    Param
    var userName: String
    Param
    var hostName: String

    override fun execute(event: Message) {
        val user = findUser(userName)
        if (user == null) {
            javabot.postMessageToChannel(event, Sofia.userNotFound(userName))
        } else {
            if (dao!!.getAdmin(user.nick, hostName) != null) {
                javabot.postMessageToChannel(event, Sofia.adminAlready(user.nick))
            } else {
                dao.create(user.nick, user.login, user.hostmask)
                javabot.postMessageToChannel(event, Sofia.adminAdded(user.nick))
            }
        }
    }

    public fun findUser(name: String): User? {
        return ircBot!!.get().userChannelDao.getUser(name)
    }

}
