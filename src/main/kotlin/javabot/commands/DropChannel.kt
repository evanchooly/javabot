package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ChannelDao
import javabot.model.Channel
import org.pircbotx.PircBotX

import javax.inject.Inject
import javax.inject.Provider

public class DropChannel : AdminCommand() {
    Inject
    private val dao: ChannelDao? = null

    Inject
    private val ircBot: Provider<PircBotX>? = null

    Param
    var channel: String

    override fun execute(event: Message) {
        val chan = dao!!.get(channel)
        if (chan != null) {
            dao.delete(chan)
            val message = Message(ircBot!!.get().userChannelDao.getChannel(channel), event.user, event.value)
            bot.postMessageToChannel(message, Sofia.channelDeleted(event.user.nick))
            event.channel.send().part(Sofia.channelDeleted(event.user.nick))
        } else {
            bot.postMessageToChannel(event, Sofia.channelUnknown(channel, event.user.nick))
        }
    }
}