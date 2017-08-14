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
        } else {
            if (tokens[0] == "list") {
                responses.addAll(handleList(event))
            }
        }

        return responses
    }

    private fun handleList(event: Message): List<Message> {
        val responses: MutableList<Message> = mutableListOf()
        val tokens = event.value.split(" ")
        if (tokens.size < 2) {
            // invalid command, right?
            responses.add(Message(event, Sofia.linksInvalidListCommand()))
        } else {
            when (tokens[1]) {
                "approved" -> {
                    responses.addAll(dao.approvedLinks(event.channel!!.name).map {
                        Message(event, Sofia.linksList(it.id.toString().substring(15), it.info))
                    }.toList())
                }
                "unapproved" -> {
                    responses.addAll(dao.unapprovedLinks(event.channel!!.name).map {
                        Message(event, Sofia.linksList(it.id.toString().substring(15), it.info))
                    }.toList())

                }
                "approve" -> {
                    if (tokens.size < 3) {
                        responses.add(Message(event, Sofia.linksInvalidApproveCommand()))
                    } else {
                        try {
                            dao.approveLink(event.channel!!.name, tokens[2])
                            responses.add(Message(event, Sofia.linksApprovedLink(tokens[2])))
                        } catch(e: IllegalArgumentException) {
                            responses.add(Message(event, Sofia.linksNotFound(tokens[2])))
                        }
                    }
                }
                "reject" -> {
                    if (tokens.size < 3) {
                        responses.add(Message(event, Sofia.linksInvalidRejectCommand()))
                    } else {
                        try {
                            dao.rejectUunapprovedLink(event.channel!!.name, tokens[2])
                            responses.add(Message(event, Sofia.linksDeletedLink(tokens[2])))
                        } catch(e: IllegalArgumentException) {
                            responses.add(Message(event, Sofia.linksNotFound(tokens[2])))
                        }
                    }
                }
                else -> responses.add(Message(event, Sofia.linksInvalidCommand(tokens[1])))
            }
        }
        return responses
    }
}