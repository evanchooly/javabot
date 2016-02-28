package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Javabot
import javabot.Message
import javabot.dao.ChannelDao
import org.pircbotx.PircBotX
import javax.inject.Inject
import javax.inject.Provider

class AddChannel @Inject constructor(javabot: Provider<Javabot>, ircBot: Provider<PircBotX>, var channelDao: ChannelDao) :
AdminCommand(javabot, ircBot) {
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
                pircBot.get().sendIRC().joinChannel(channel.name)
            } else {
                pircBot.get().sendIRC().joinChannel(channel.name, channel.key)
            }

            responses.add(Message(pircBot.get().userChannelDao.getChannel(channelName), event, Sofia.adminJoinedChannel(event.user.nick)))
        } else {
            responses.add(Message(event, Sofia.adminBadChannelName()))
        }

        return responses
    }
}
