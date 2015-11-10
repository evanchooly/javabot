package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import com.google.inject.Provider
import javabot.Message
import org.pircbotx.PircBotX
import org.pircbotx.User
import javax.inject.Inject

public class AddAdmin : AdminCommand() {
    @Inject
    private lateinit var ircBot: Provider<PircBotX>
    @Parameter(required = true)
    lateinit var userName: String
    @Parameter(required = true)
    lateinit var hostName: String

    override fun execute(event: Message): List<Message> {
        var responses = arrayListOf<Message>()
        val user = findUser(userName)
        if (user == null) {
            responses.add(Message(event, Sofia.userNotFound(userName)))
        } else {
            if (adminDao.getAdmin(user.nick, hostName) != null) {
                responses.add(Message(event, Sofia.adminAlready(user.nick)))
            } else {
                adminDao.create(user.nick, user.login, user.hostmask)
                responses.add(Message(event, Sofia.adminAdded(user.nick)))
            }
        }

        return responses
    }

    public fun findUser(name: String): User? {
        return ircBot.get().userChannelDao.getUser(name)
    }

}
