package javabot.commands

import com.antwerkz.sofia.Sofia
import com.google.inject.Provider
import javabot.Message
import org.pircbotx.PircBotX
import org.pircbotx.User
import javax.inject.Inject

public class AddAdmin : AdminCommand() {
    @Inject
    private lateinit val ircBot: Provider<PircBotX>
    @Param
    lateinit var userName: String
    @Param
    lateinit var hostName: String

    override fun execute(event: Message) {
        val user = findUser(userName)
        if (user == null) {
            javabot.get().postMessageToChannel(event, Sofia.userNotFound(userName))
        } else {
            if (adminDao.getAdmin(user.nick, hostName) != null) {
                javabot.get().postMessageToChannel(event, Sofia.adminAlready(user.nick))
            } else {
                adminDao.create(user.nick, user.login, user.hostmask)
                javabot.get().postMessageToChannel(event, Sofia.adminAdded(user.nick))
            }
        }
    }

    public fun findUser(name: String): User? {
        return ircBot.get().userChannelDao.getUser(name)
    }

}
