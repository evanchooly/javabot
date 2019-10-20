package javabot

import com.antwerkz.sofia.Sofia
import javabot.model.Channel
import javabot.model.JavabotUser
import javabot.operations.TellSubject
import org.slf4j.LoggerFactory
import java.lang.String.format
import java.text.Normalizer

open class Message(val channel: Channel? = null, val user: JavabotUser, val value: String, val target: JavabotUser? = null,
                   val triggered: Boolean = true) {
    companion object {
        private val LOG = LoggerFactory.getLogger(Message::class.java)

        private fun normalized(input: String): String {
            val content = Normalizer.normalize(input, Normalizer.Form.NFD)
            return if (content.length > 1 && content[1] == '\u0303') {
                "~" + content.substring(0, 1).replace(Regex("\\p{M}"), "") + content.substring(2)
            } else {
                input
            }
        }

        fun extractContentFromMessage(bot: Javabot, channel: Channel?, user: JavabotUser,
                                      startString: String, botNick: String, message: String): Message {
            try {
                var triggered = false

                var content = normalized(message)

                for (start in arrayOf(startString, botNick)) {
                    if (content.startsWith(start)) {
                        triggered = true
                        content = if (triggered) content.substring(start.length).trim() else content.trim()
                    }
                }

                content = content.dropWhile { it in arrayOf(':', ',') }.trim()

                if (isTellCommand(startString, content)) {
                    val tellSubject = if (content.startsWith("tell ")) parseLonghand(bot, content) else parseShorthand(startString,
                            content)

                    if (tellSubject != null) {
                        return Message(channel, user, tellSubject.subject, tellSubject.target, triggered)
                    } else {
                        return Message(channel, user, Sofia.factoidTellSyntax(user.nick), triggered = triggered)
                    }
                }

                return Message(channel, user, content, triggered = triggered)
            } catch (e: Exception) {
                LOG.error("Failed to parse: ${message} in channel ${channel}", e)
                throw e
            }
        }

        private fun isTellCommand(startString: String, value: String): Boolean {
            return value.startsWith("tell ") || "" != startString && value.startsWith(startString)
        }

        private fun parseLonghand(bot: Javabot, content: String): TellSubject? {
            val body = content.substring("tell ".length)
            val nick = body.substring(0, body.indexOf(" "))
            val about = body.indexOf("about ") + 5
            return if (about >= 0) TellSubject(bot.getUser(nick), body.substring(about).trim()) else return null
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

    /**
     * Does this target a user with the message?
     */
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
