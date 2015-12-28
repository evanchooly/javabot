package javabot

import com.antwerkz.sofia.Sofia
import javabot.operations.TellSubject
import org.pircbotx.Channel
import org.pircbotx.PircBotX
import org.pircbotx.User

import java.lang.String.format

open class Message(val channel: Channel? = null, val user: User, val value: String, val target: User? = null ) {
    val tell: Boolean = target != null

    public constructor(user: User, value: String): this(null, user, value)

    public constructor(channel: Channel, message: Message, value: String): this(channel, message.user, value, message.target)

    public constructor(message: Message, value: String) : this(message.channel, message.user, value, message.target )

    override fun toString(): String {
        return "Message{channel=${channel?.name ?: ""}, user=${user.nick}, message='${value}', tell=${tell}}"
    }

    public fun massageTell(): String {
        return if (tell && !value.contains(user.nick)) format("%s, %s", user.nick, value) else value
    }

    fun extractContentFromMessage(ircBot: PircBotX, startString: String): Message? {
        var content = value.substring(startString.length).trim()
        while (!content.isEmpty() && (content[0] == ':' || content[0] == ',')) {
            content = content.substring(1).trim()
        }
        if (isTellCommand(startString)) {
            val tellSubject = parseTellSubject(ircBot, startString, content)
            if (tellSubject != null) {
                return Message(channel, user, tellSubject.subject, tellSubject.target)
            } else {
                return Message(this, Sofia.factoidTellSyntax(user.nick))
            }
        }

        return if (!content.isEmpty()) Message(channel, user, content) else null
    }

    private fun isTellCommand(startString: String): Boolean {
        return value.startsWith("tell ") || "" != startString && value.startsWith(startString)
    }

    private fun parseTellSubject(ircBot: PircBotX, startString: String, content: String): TellSubject? {
        return if (content.startsWith("tell ")) parseLonghand(ircBot, content) else parseShorthand(ircBot, startString, content)
    }

    private fun parseLonghand(ircBot: PircBotX, content: String): TellSubject? {
        val message = content
        val body = message.substring("tell ".length)
        val nick = body.substring(0, body.indexOf(" "))
        val about = body.indexOf("about ")
        if (about < 0) {
            return null
        }
        val thing = body.substring(about + "about ".length)
        return TellSubject(ircBot.userChannelDao.getUser(nick), thing)
    }

    private fun parseShorthand(ircBot: PircBotX, startString: String, content: String): TellSubject? {
        var target = content
        if (target.startsWith(startString)) {
            target = target.substring(startString.length).trim()
        }
        val space = target.indexOf(' ')
        var tellSubject: TellSubject? = null
        if (space >= 0) {
            val user = ircBot.userChannelDao.getUser(target.substring(0, space))
            tellSubject = TellSubject(user, target.substring(space + 1).trim())
        }
        return tellSubject
    }
}
