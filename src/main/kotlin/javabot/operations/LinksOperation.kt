package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChangeDao
import javabot.dao.ChannelDao
import java.net.URL
import javax.inject.Inject

class LinksOperation @Inject constructor(bot: Javabot,
                                         adminDao: AdminDao,
                                         var changeDao: ChangeDao,
                                         var channelDao: ChannelDao) : BotOperation(bot, adminDao) {

    /**
     * @return true if the message has been handled
     */
    override fun handleMessage(event: Message): List<Message> {
        val responses = mutableListOf<Message>()
        val tokens = event.value.split(" ")
        if (tokens.isEmpty()) {
            return emptyList()
        }

        responses.addAll(when (tokens[0].toLowerCase()) {
            "submit" -> handleSubmit(event)
            else -> emptyList()
        })
        return responses
    }

    private fun handleSubmit(event: Message): List<Message> {
        val responses: MutableList<Message> = mutableListOf()
        val needsChannel = if (event.channel != null) {
            event.channel.name.isEmpty()
        } else true

        val tokens = event.value.split(" ").toMutableList()
        tokens.removeAt(0) // remove "submit"
        try {
            val channel = extractChannel(tokens, needsChannel, event)
            if (!bot.adapter.isOnChannel(channel, event.user.nick)) {
                responses.add(Message(event, Sofia.linksNotOnChannel()))
            } else {
                // let's find a url; if it's there, we have a valid link, submit it.
                val firstUrl = tokens.mapNotNull {
                    try {
                        URL(it)
                    } catch (e: Exception) {
                        null
                    }
                }
                        .firstOrNull()
                if (firstUrl != null) {
                    // we have a url! Submit this puppy as is after stripping off "submit"
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
        if (needsChannel && internalChannel.isEmpty()) {
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

    private fun isValidChannel(token: String): Boolean {
        return bot.adapter.isChannel(token)
    }

    private fun isChannelName(token: String): Boolean {
        return token.matches(Regex("##?\\p{Alpha}\\p{Alpha}*"))
    }
}

class WrongChannelException(c: String, val channelName: String) : Throwable(c)

class NoChannelException(s: String) : Throwable(s)
