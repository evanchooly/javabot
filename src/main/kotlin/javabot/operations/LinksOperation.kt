package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChangeDao
import javabot.dao.ChannelDao
import javabot.dao.LinkDao
import javabot.model.JavabotUser
import javabot.model.Link
import org.pircbotx.Channel
import org.pircbotx.User
import org.pircbotx.UserChannelDao
import org.slf4j.LoggerFactory
import java.net.URL
import javax.inject.Inject

class LinksOperation @Inject constructor(bot: Javabot,
                                         adminDao: AdminDao,
                                         var dao: LinkDao,
                                         var changeDao: ChangeDao,
                                         var channelDao: ChannelDao) : BotOperation(bot, adminDao) {
    companion object {
        private val log = LoggerFactory.getLogger(LinksOperation::class.java)
    }

    /**
     * @return true if the message has been handled
     */
    override fun handleMessage(event: Message): List<Message> {
        val responses: MutableList<Message> = mutableListOf()
        val tokens = event.value.split(" ").toMutableList()
        if (tokens.isEmpty()) {
            return emptyList()
        }

        responses.addAll(when (tokens[0].toLowerCase()) {
            "submit" -> handleSubmit(event)
            "list" -> handleList(event)
            else -> emptyList()
        })
        return responses
    }

    fun handleList(event: Message): List<Message> {
        val responses: MutableList<Message> = mutableListOf()
        val needsChannel = if (event.channel != null) {
            event.channel.name.isNullOrEmpty()
        } else true
        val tokens = event.value.split(" ").toMutableList()
        // remove the "list" command, which is handled by the calling method
        tokens.removeAt(0)
        when (tokens[0].toLowerCase()) {
            "approved" -> showMessageLists(tokens, needsChannel, event, responses, dao::approvedLinks, tokens[0])
            "unapproved" -> showMessageLists(tokens, needsChannel, event, responses, dao::unapprovedLinks, tokens[0], true)
            "approve" -> handleVerb(tokens, needsChannel, event, responses, dao::approveLink, tokens[0])
            "reject" -> handleVerb(tokens, needsChannel, event, responses, dao::rejectUnapprovedLink, tokens[0])
            "help" -> showHelp(event, responses)
            else -> {
                responses.add(Message(event, Sofia.linksInvalidCommand(tokens[0])))
            }
        }
        return responses
    }

    private fun showHelp(event: Message, responses: MutableList<Message>) {
        responses.addAll(
                listOf(
                        "Available commands for link management:",
                        "list approved [channel] [count] - shows the [count] approved links for channel [channel]",
                        "list unapproved [channel] [count] - shows the [count] unapproved links for channel [channel]",
                        "list approve [channel] [key] - approves the link with key [key] for channel [channel]",
                        "list reject [channel] [key] - deletes the link with key [key] for channel [channel]",
                        "list help - shows this text"
                )
                        .map { Message(event.user, it) }
        )
    }

    private fun handleSubmit(event: Message): List<Message> {
        val responses: MutableList<Message> = mutableListOf()
        val needsChannel = if (event.channel != null) {
            event.channel.name.isNullOrEmpty()
        } else true

        val tokens = event.value.split(" ").toMutableList()
        tokens.removeAt(0) // remove "submit"
        try {
            val channel = extractChannel(tokens, needsChannel, event)
            if (!bot.adapter.isOnChannel(channel, event.user.nick)) {
                responses.add(Message(event, Sofia.linksNotOnChannel()))
            } else {
                // let's find a url; if it's there, we have a valid link, submit it.
                val firstUrl = tokens
                        .map {
                            try {
                                URL(it)
                            } catch (e: Exception) {
                                null
                            }
                        }
                        .filterNotNull()
                        .firstOrNull()
                if (firstUrl != null) {
                    // we have a url! Submit this puppy as is after stripping off "submit"
                    dao.addLink(channel, event.user.userName, firstUrl.toString(), event.value.substring("submit ".length))
                    responses.add(Message(event, Sofia.linksAccepted(firstUrl, channel)))
                } else {
                    responses.add(Message(event, Sofia.linksRejectedNoUrl()))
                }
            }
        } catch (e: NoChannelException) {
            responses.add(Message(event, Sofia.linksNoChannel()))
        }
        return responses
    }

    private fun handleVerb(tokens: MutableList<String>,
                           needsChannel: Boolean,
                           event: Message,
                           responses: MutableList<Message>,
                           modifyFunction: (String, String) -> Unit,
                           command: String) {
        tokens.removeAt(0)
        try {
            val channel = extractChannel(tokens, needsChannel, event)
            if (!bot.adapter.isOp(event.user.nick, channel)) {
                responses.add(Message(event, "You need to be an op on $channel to do that"))
            } else {
                val key = extractKey(tokens)
                try {
                    modifyFunction(channel, key)
                    responses.add(Message(event, "blah blah blah"))
                } catch (e: IllegalArgumentException) {
                    responses.add(Message(event, Sofia.linksNotFound(key)))
                }
            }
        } catch (e: NoChannelException) {
            responses.add(Message(event, Sofia.linksNoChannel()))
        } catch (e: NoMessageKeyException) {
            responses.add(Message(event, Sofia.linksNoKeySpecified(command)))
        }
    }

    private fun extractKey(tokens: MutableList<String>): String {
        return if (tokens.isNotEmpty()) {
            val tok = tokens[0]
            tokens.removeAt(0)
            tok
        } else {
            throw NoMessageKeyException("No message key specified")
        }
    }

    /**
     * Ugh. Okay, so:
     * 1. See if the first token is a channel. If it is, pull it off and retain it internally...
     * 2. If we have an internal and external channel, they should match. If not, throw a wrong channel message.
     * 3. If we NEED a channel, and the internal channel is empty, throw a no channel message.
     * 4. return either the internal channel (if present) or the external channel.
     * */
    private fun extractChannel(tokens: MutableList<String>, needsChannel: Boolean, event: Message): String {
        // step 1
        val internalChannel = if (tokens.isNotEmpty() && isChannelName(tokens[0])) {
            tokens.removeAt(0)
        } else {
            ""
        }
        // step 2
        if (event.channel != null && internalChannel.isNotEmpty() && event.channel.name != internalChannel) {
            throw WrongChannelException(internalChannel, event.channel.name)
        }
        // step 3
        if (needsChannel && internalChannel.isNullOrEmpty()) {
            throw NoChannelException("no channel specified") // how did we get here?!
        }
        // step 4
        val chan = if (internalChannel.isNotEmpty()) {
            internalChannel
        } else {
            if (event.channel != null) {
                event.channel.name
            } else {
                throw NoChannelException("no channel specified") // how did we get here?!
            }
        }
        if (!isValidChannel(chan)) {
            throw NoChannelException(chan)
        }
        return chan
    }

    private fun showMessageLists(tokens: MutableList<String>,
                                 needsChannel: Boolean,
                                 event: Message,
                                 responses: MutableList<Message>,
                                 listFunction: (String) -> List<Link>,
                                 status: String,
                                 needsOps: Boolean = false) {
        tokens.removeAt(0)
        try {
            val channel = extractChannel(tokens, needsChannel, event)
            val count = getOptionalCount(tokens)
            if (needsOps && !bot.adapter.isOp(event.user.nick, channel)) {
                responses.add(Message(event, "You need to be an op on $channel to do that"))
            } else {
                responses.addAll(
                        listFunction(channel)
                                .map {
                                    val id = it.id.toString()
                                    Message(event.user, Sofia.linksList(id.substring(id.length - 5), it.info))
                                }
                                .toList()
                                .take(count)
                )
                if (responses.size == 0) {
                    responses.add(Message(event, Sofia.linksNoLinksOfStatus(status, channel)))
                }
            }
        } catch (e: WrongChannelException) {
            responses.add(Message(event, Sofia.linksWrongChannel(e.channelName)))
        } catch (e: NoChannelException) {
            responses.add(Message(event, Sofia.linksNoChannel()))
        }
    }

    private fun getOptionalCount(tokens: MutableList<String>): Int {
        return try {
            if (tokens.isNotEmpty()) {
                tokens[0].toInt()
            } else {
                5
            }
        } catch (e: Exception) {
            5
        }
    }

    private fun isValidChannel(token: String): Boolean {
        return bot.adapter.isChannel(token)
    }

    private fun isChannelName(token: String): Boolean {
        return token.matches(Regex("##?\\p{Alpha}\\p{Alpha}*"))
    }
}

class WrongChannelException(c: String, val channelName: String) : Throwable(c) {
}

class NoChannelException(s: String) : Throwable(s) {
}

class NoMessageKeyException(s: String) : Throwable(s) {
}
