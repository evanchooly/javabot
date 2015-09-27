package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ChannelDao
import org.pircbotx.PircBotX
import javax.inject.Inject
import javax.inject.Provider

public class AddChannel : AdminCommand() {
    @Inject
    lateinit val channelDao: ChannelDao
    @Inject
    lateinit val ircBot: Provider<PircBotX>

    @Param
    lateinit var channelName: String
    @Param(defaultValue = "true", required = false)
    lateinit var logged: String
    @Param(defaultValue = "", required = false)
    lateinit var password: String

    override fun execute(event: Message) {
        if (channelName.startsWith("#")) {
            var channel = channelDao.get(channelName)
            val isLogged = java.lang.Boolean.valueOf(logged)
            if (channel == null) {
                channel = channelDao.create(channelName, isLogged, password)
            } else {
                channel.logged = isLogged
                channelDao.save(channel)
            }

            bot.postMessageToChannel(event, if (isLogged) Sofia.adminJoiningLoggedChannel(channelName) else Sofia.adminJoiningChannel(
                  channelName))
            if (channel.key == null) {
                ircBot.get().sendIRC().joinChannel(channel.name)
            } else {
                ircBot.get().sendIRC().joinChannel(channel.name, channel.key)
            }

            bot.postMessageToChannel(Message(event, ircBot.get().userChannelDao.getChannel(channelName)),
                  Sofia.adminJoinedChannel(event.user.nick))
        } else {
            bot.postMessageToChannel(event, Sofia.adminBadChannelName())
        }
    }
}
