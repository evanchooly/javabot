package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import com.google.inject.Inject
import com.google.inject.Provider
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import org.pircbotx.PircBotX

class AddChannel @Inject constructor(bot: Javabot, adminDao: AdminDao, ircBot: Provider<PircBotX>, var channelDao: ChannelDao) :
AdminCommand(bot, adminDao, ircBot) {
    @Parameter(required = true)
    lateinit var channelName: String
    @Parameter(required = false)
    var logged = true
    @Parameter(required = false, password = true)
    var password: String = ""

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        if (channelName.startsWith("#")) {
            var channel = channelDao.get(channelName)
            val isLogged = java.lang.Boolean.valueOf(logged)
            if (channel == null) {
                channel = channelDao.create(channelName, isLogged, password)
            } else {
                channel.logged = isLogged
                channelDao.save(channel)
            }

            responses.add(Message(event,
                    if (isLogged)
                        Sofia.adminJoiningLoggedChannel(channelName)
                    else
                        Sofia.adminJoiningChannel(channelName)))
            if (channel.key == null) {
                ircBot.get().sendIRC().joinChannel(channel.name)
            } else {
                ircBot.get().sendIRC().joinChannel(channel.name, channel.key)
            }

            responses.add(Message(ircBot.get().userChannelDao.getChannel(channelName), event, Sofia.adminJoinedChannel(event.user.nick)))
        } else {
            responses.add(Message(event, Sofia.adminBadChannelName()))
        }

        return responses
    }
}
