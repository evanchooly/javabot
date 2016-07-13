package javabot

import com.antwerkz.sofia.Sofia
import javabot.model.Channel
import javabot.model.JavabotUser
import javabot.operations.TellSubject
import java.lang.String.format

open class Message(val channel: Channel? = null, val user: JavabotUser, val value: String, val target: JavabotUser? = null) {
    companion object {
        fun extractContentFromMessage(channel: Channel?, user: JavabotUser, startString: String, message: String): Message {
            var content = message.substring(startString.length).trim()
            while (!content.isEmpty() && (content[0] == ':' || content[0] == ',')) {
                content = content.substring(1).trim()
            }
            if (isTellCommand(startString, content)) {
                val tellSubject = if (content.startsWith("tell ")) parseLonghand(content) else parseShorthand(startString, content)

                if (tellSubject != null) {
                    return Message(channel, user, tellSubject.subject, tellSubject.target)
                } else {
                    return Message(channel, user, Sofia.factoidTellSyntax(user.nick))
                }
            }

            return Message(channel, user, content)
        }

        private fun isTellCommand(startString: String, value: String): Boolean {
            return value.startsWith("tell ") || "" != startString && value.startsWith(startString)
        }

        private fun parseLonghand(content: String): TellSubject? {
            val body = content.substring("tell ".length)
            val nick = body.substring(0, body.indexOf(" "))
            val about = body.indexOf("about ") + 5
            return if (about >= 0) TellSubject(JavabotUser(nick), body.substring(about).trim()) else return null
        }

        private fun parseShorthand(startString: String, content: String): TellSubject? {
            var target = content
            if (target.startsWith(startString)) {
                target = target.substring(startString.length).trim()
            }
            val space = target.indexOf(' ')
            val about = target.substring(space + 1)
            val nick = target.substring(0, space)
            return if (space >= 0) TellSubject(JavabotUser(nick), about.trim()) else null
        }

    }

    val tell: Boolean = target != null

    constructor(user: JavabotUser, value: String) : this(null, user, value)

    constructor(channel: Channel, message: Message, value: String) : this(channel, message.user, value, message.target)

    constructor(message: Message, value: String) : this(message.channel, message.user, value, message.target)

    fun massageTell(): String {
        return if (tell && target != null && !value.contains(target.nick)) format("%s, %s", target.nick, value) else value
    }

    override fun toString(): String {
        return "Message{channel=${channel?.name ?: ""}, user=${user.nick}, message='$value', tell=${tell}}"
    }

}
