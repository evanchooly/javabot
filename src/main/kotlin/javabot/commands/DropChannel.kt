package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javax.inject.Inject

class DropChannel @Inject constructor(bot: Javabot, adminDao: AdminDao, var channelDao: ChannelDao) : AdminCommand(bot, adminDao) {

    @Parameter(required = true)
    lateinit var channel: String

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val chan = channelDao.get(channel)
        if (chan != null) {
            channelDao.delete(chan)
            val message = Message(chan, event.user, event.value)
            responses.add(Message(message, Sofia.channelDeleted(event.user.nick)))
            bot.leaveChannel(chan, event.user)
        } else {
            responses.add(Message(event, Sofia.channelUnknown(channel, event.user.nick)))
        }
        return responses
    }
}