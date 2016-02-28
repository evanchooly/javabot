package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.util.QueryParam
import org.apache.commons.lang.StringUtils
import org.pircbotx.PircBotX
import java.lang.String.format
import javax.inject.Inject
import javax.inject.Provider

class ListChannels @Inject constructor(bot: Javabot, adminDao: AdminDao, ircBot: Provider<PircBotX>, var channelDao: ChannelDao) :
        AdminCommand(bot, adminDao, ircBot) {

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