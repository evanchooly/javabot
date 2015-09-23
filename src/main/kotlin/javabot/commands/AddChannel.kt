package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ChannelDao
import javabot.model.Channel
import org.pircbotx.PircBotX

import javax.inject.Inject
import javax.inject.Provider

public class AddChannel : AdminCommand() {
    Inject
    private val dao: ChannelDao? = null
    Inject
    private val ircBot: Provider<PircBotX>? = null

    Param
    var name: String
    Param(defaultValue = "true", required = false)
    var logged: String
    Param(defaultValue = "", required = false)
    var password: String

    override fun execute(event: Message) {
        if (name.startsWith("#")) {
            var channel = dao!!.get(name)
            val isLogged = java.lang.Boolean.valueOf(logged)
            if (channel == null) {
                channel = dao.create(name, isLogged, password)
            } else {
                channel.logged = isLogged
                dao.save(channel)
            }

            bot.postMessageToChannel(event, if (isLogged) Sofia.adminJoiningLoggedChannel(name) else Sofia.adminJoiningChannel(name))
            if (channel!!.key == null) {
                ircBot!!.get().sendIRC().joinChannel(channel.name)
            } else {
                ircBot!!.get().sendIRC().joinChannel(channel.name, channel.key)
            }

            bot.postMessageToChannel(Message(event, ircBot.get().userChannelDao.getChannel(name)),
                  Sofia.adminJoinedChannel(event.user.nick))
        } else {
            bot.postMessageToChannel(event, Sofia.adminBadChannelName())
        }
    }
}
