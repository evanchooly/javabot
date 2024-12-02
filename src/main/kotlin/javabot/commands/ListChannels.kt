package javabot.commands

import com.antwerkz.sofia.Sofia
import jakarta.inject.Inject
import java.lang.String.format
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.util.QueryParam

class ListChannels
@Inject
constructor(bot: Javabot, adminDao: AdminDao, var channelDao: ChannelDao) :
    AdminCommand(bot, adminDao) {

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val channels = channelDao.find(QueryParam(0, Integer.MAX_VALUE))
        responses.add(Message(event, Sofia.adminListChannelsPreamble(event.user.nick)))
        val names =
            channels.joinToString(
                ", ",
                transform = { channel ->
                    format("%s %s", channel.name, if (channel.logged) "(logged)" else "")
                }
            )
        responses.add(Message(event.user, names))

        return responses
    }
}
