package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Javabot
import javabot.Message
import javabot.dao.ChannelDao
import org.pircbotx.PircBotX
import javax.inject.Inject
import javax.inject.Provider

class DropChannel @Inject constructor(javabot: Provider<Javabot>, ircBot: Provider<PircBotX>, var channelDao: ChannelDao) :
        AdminCommand (javabot, ircBot) {

    @Parameter(required = true)
    lateinit var channel: String

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val chan = channelDao.get(channel)
        if (chan != null) {
            channelDao.delete(chan)
            val deleted = pircBot.get().userChannelDao.getChannel(channel)
            val message = Message(deleted, event.user, event.value)
            responses.add(Message(message, Sofia.channelDeleted(event.user.nick)))
            deleted.send().part(Sofia.channelDeleted(event.user.nick))
        } else {
            responses.add(Message(event, Sofia.channelUnknown(channel, event.user.nick)))
        }
        return responses
    }
}