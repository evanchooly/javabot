package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ChannelDao
import javabot.dao.util.QueryParam
import javabot.model.Channel
import org.apache.commons.lang.StringUtils

import javax.inject.Inject
import java.util.stream.Collectors

import java.lang.String.format

public class ListChannels : AdminCommand() {
    Inject
    private val dao: ChannelDao? = null

    override fun execute(event: Message) {
        val channels = dao!!.find(QueryParam(0, Integer.MAX_VALUE))
        bot.postMessageToChannel(event, Sofia.adminListChannelsPreamble(event.user.nick))
        val names = channels.stream().map(
              { channel -> format("%s %s", channel.name, if (channel.getLogged()) "(logged)" else "") }).collect(
              Collectors.toList<String>())
        bot.postMessageToUser(event.user, StringUtils.join(names, ", "))
    }
}