package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Message
import javabot.dao.ChannelDao
import org.pircbotx.PircBotX
import javax.inject.Inject
import javax.inject.Provider

public class DropChannel : AdminCommand() {
    @Inject
    lateinit var channelDao: ChannelDao

    @Inject
    lateinit var ircBot: Provider<PircBotX>

    @Parameter
    lateinit var channel: String

    override fun execute(event: Message) {
        val chan = channelDao.get(channel)
        if (chan != null) {
            channelDao.delete(chan)
            val deleted = ircBot.get().userChannelDao.getChannel(channel)
            val message = Message(deleted, event.user, event.value)
            bot.postMessageToChannel(message, Sofia.channelDeleted(event.user.nick))
            deleted.send().part(Sofia.channelDeleted(event.user.nick))
        } else {
            bot.postMessageToChannel(event, Sofia.channelUnknown(channel, event.user.nick))
        }
    }
}