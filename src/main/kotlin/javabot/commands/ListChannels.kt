package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.util.QueryParam
import org.apache.commons.lang.StringUtils
import java.lang.String.format
import javax.inject.Inject

class ListChannels @Inject constructor(bot: Javabot, adminDao: AdminDao, var channelDao: ChannelDao) : AdminCommand(bot, adminDao) {

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val channels = channelDao.find(QueryParam(0, Integer.MAX_VALUE))
        responses.add(Message(event, Sofia.adminListChannelsPreamble(event.user.nick)))
        val names = channels.map(
                { channel -> format("%s %s", channel.name, if (channel.logged) "(logged)" else "") })
        responses.add(Message(event.user, StringUtils.join(names, ", ")))

        return responses
    }
}