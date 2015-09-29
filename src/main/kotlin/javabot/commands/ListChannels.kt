package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ChannelDao
import javabot.dao.util.QueryParam
import org.apache.commons.lang.StringUtils
import java.lang.String.format
import javax.inject.Inject

public class ListChannels : AdminCommand() {
    @Inject
    lateinit var channelDao: ChannelDao

    override fun execute(event: Message) {
        val channels = channelDao.find(QueryParam(0, Integer.MAX_VALUE))
        bot.postMessageToChannel(event, Sofia.adminListChannelsPreamble(event.user.nick))
        val names = channels.map(
              { channel -> format("%s %s", channel.name, if (channel.logged) "(logged)" else "") })
        bot.postMessageToUser(event.user, StringUtils.join(names, ", "))
    }
}