package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.FactoidDao
import javabot.model.Factoid
import org.pircbotx.PircBotX
import org.pircbotx.User
import org.slf4j.LoggerFactory
import java.lang.String.format
import java.util.HashSet
import javax.inject.Inject
import javax.inject.Provider

public class GetFactoidOperation : BotOperation(), StandardOperation {

    @Inject
    lateinit var factoidDao: FactoidDao

    @Inject
    lateinit var ircBot: Provider<PircBotX>

    override fun getPriority(): Int {
        return Integer.MIN_VALUE
    }

    override fun handleMessage(event: Message): Boolean {
        return tell(event) || getFactoid(null, event, HashSet<String>())
    }

    private fun getFactoid(subject: TellSubject?, event: Message, backtrack: MutableSet<String>): Boolean {
        var message = event.value
        if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
            message = message.substring(0, message.length() - 1)
        }
        val firstWord = message.split(" ")[0]
        val params = message.substring(firstWord.length()).trim()
        var factoid = factoidDao.getFactoid(message.toLowerCase())
        if (factoid == null) {
            factoid = factoidDao.getParameterizedFactoid(firstWord)
        }

        return factoid != null && getResponse(subject, event, backtrack, params, factoid)
    }

    private fun getResponse(subject: TellSubject?, event: Message, backtrack: MutableSet<String>,
                            replacedValue: String, factoid: Factoid): Boolean {
        val sender = event.user.nick
        val message = factoid.evaluate(subject, sender, replacedValue)
        if (message.startsWith("<see>")) {
            if (backtrack.contains(message)) {
                bot.postMessageToChannel(event, Sofia.factoidLoop(message))
                return true
            } else {
                backtrack.add(message)
                return getFactoid(subject, Message(event, message.substring(5).trim()), backtrack)
            }
        } else if (message.startsWith("<reply>")) {
            bot.postMessageToChannel(event, message.substring("<reply>".length()))
            return true
        } else if (message.startsWith("<action>")) {
            try {
                bot.postAction(event.channel!!, message.substring("<action>".length()))
            } catch (e: Exception) {
                LOG.error(format("NPE:  subject = [%s], event = [%s], backtrack = [%s], replacedValue = [%s], factoid = [%s]",
                      subject, event, backtrack, replacedValue, factoid))
                e.printStackTrace()
            }

            return true
        } else {
            bot.postMessageToChannel(event, message)
            return true
        }
    }

    private fun tell(event: Message): Boolean {
        val message = event.value
        val channel = event.channel!!
        val sender = event.user
        var handled = false
        if (isTellCommand(message)) {
            val tellSubject = parseTellSubject(event)
            if (tellSubject == null) {
                bot.postMessageToChannel(event, Sofia.factoidTellSyntax(sender.nick))
                handled = true
            } else {
                var targetUser: User? = tellSubject.target
                if (targetUser != null) {
                    if ("me".equals(targetUser.nick, ignoreCase = true)) {
                        targetUser = sender
                    }
                    val thing = tellSubject.subject
                    if (targetUser.nick.equals(bot.getNick(), ignoreCase = true)) {
                        bot.postMessageToChannel(event, Sofia.botSelfTalk())
                        handled = true
                    } else {
                        if (!bot.isOnCommonChannel(targetUser)) {
                            bot.postMessageToChannel(event,
                                  Sofia.userNotInChannel(targetUser.nick, channel.name))
                            handled = true
                        } else if (sender.nick == channel.name && !bot.isOnCommonChannel(targetUser)) {
                            bot.postMessageToChannel(event, Sofia.userNoSharedChannels())
                            handled = true
                        } else if (thing.endsWith("++") || thing.endsWith("--")) {
                            bot.postMessageToChannel(event, Sofia.notAllowed())
                            handled = true
                        } else {
                            handled = bot.getResponses(Message(channel, targetUser, thing, sender), event.user)
                        }
                    }
                }
            }
        }
        return handled
    }

    private fun parseTellSubject(event: Message): TellSubject? {
        val message = event.value
        if (message.startsWith("tell ")) {
            return parseLonghand(event)
        }
        return parseShorthand(event)
    }

    private fun parseLonghand(event: Message): TellSubject? {
        val message = event.value
        val body = message.substring("tell ".length())
        val nick = body.substring(0, body.indexOf(" "))
        val about = body.indexOf("about ")
        if (about < 0) {
            return null
        }
        val thing = body.substring(about + "about ".length())
        return TellSubject(ircBot.get().userChannelDao.getUser(nick), thing)
    }

    private fun parseShorthand(event: Message): TellSubject? {
        var target = event.value
        for (start in bot.startStrings) {
            if (target.startsWith(start)) {
                target = target.substring(start.length()).trim()
            }
        }
        val space = target.indexOf(' ')
        var tellSubject: TellSubject? = null
        if (space >= 0) {
            val user = ircBot.get().userChannelDao.getUser(target.substring(0, space))
            tellSubject = TellSubject(user, target.substring(space + 1).trim())
        }
        return tellSubject
    }

    private fun isTellCommand(message: String): Boolean {
        return message.startsWith("tell ") || message.startsWith("~")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(GetFactoidOperation::class.java)
    }
}