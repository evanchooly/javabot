package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChangeDao
import javabot.dao.ChannelDao
import javabot.dao.LinkDao
import org.slf4j.LoggerFactory
import java.net.URL
import javax.inject.Inject

class LinksOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var dao: LinkDao, var changeDao: ChangeDao,
                                         var channelDao: ChannelDao) : BotOperation(bot, adminDao) {
    companion object {
        private val log = LoggerFactory.getLogger(LinksOperation::class.java)
    }

    /**
     * @return true if the message has been handled
     */
    override fun handleMessage(event: Message): List<Message> {
        val responses: MutableList<Message> = mutableListOf()
        val tokens = event.value.split(" ")
        if (tokens.isEmpty()) {
            return emptyList()
        }
        if (event.channel == null) {
            return emptyList()
        }
        if (tokens[0].toLowerCase() == "submit") {
            // let's find a url; if it's there, we have a valid link, submit it.
            val firstUrl = tokens
                    .map {
                        try {
                            URL(it)
                        } catch(e: Exception) {
                            null
                        }
                    }
                    .filterNotNull()
                    .firstOrNull()
            if (firstUrl != null) {
                // we have a url! Submit this puppy as is after stripping off "submit"
                dao.addLink(event.channel.name, event.user.userName, firstUrl.toString(), event.value.substring("submit ".length))
                responses.add(Message(event, Sofia.linksAccepted(firstUrl, event.channel.name)))
            } else {
                responses.add(Message(event, Sofia.linksRejected()))
            }
        }
        return responses
    }
}