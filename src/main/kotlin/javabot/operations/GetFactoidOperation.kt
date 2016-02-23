package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Action
import javabot.Message
import javabot.dao.FactoidDao
import javabot.model.Factoid
import org.pircbotx.PircBotX
import org.slf4j.LoggerFactory
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

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()

        validateTell(responses, event)

        if (responses.isEmpty()) {
            getFactoid(responses, event, HashSet<String>())
        }

        return responses
    }

    private fun getFactoid(responses: MutableList<Message>, event: Message, backtrack: MutableSet<String>) {
        var message = event.value
        if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
            message = message.substring(0, message.length - 1)
        }
        val firstWord = message.split(" ")[0]
        val params = message.substring(firstWord.length).trim()
        var factoid = factoidDao.getFactoid(message.toLowerCase())
        if (factoid == null) {
            factoid = factoidDao.getParameterizedFactoid(firstWord)
        }

        if (factoid != null) {
            getResponse(responses, event, backtrack, params, factoid)
        }
    }

    private fun getResponse(responses: MutableList<Message>, event: Message, backtrack: MutableSet<String>, replacedValue: String,
                            factoid: Factoid) {
        val sender = event.user.nick
        val message = factoid.evaluate(event.target, sender, replacedValue)
        if (message.startsWith("<see>")) {
            if (backtrack.contains(message)) {
                responses.add(Message(event, Sofia.factoidLoop(message)))
            } else {
                backtrack.add(message)
                return getFactoid(responses, Message(event, message.substring(5).trim()), backtrack)
            }
        } else if (message.startsWith("<reply>")) {
            responses.add(Message(event, message.substring("<reply>".length)))
        } else if (message.startsWith("<action>")) {
            try {
                responses.add(Action(event, message.substring("<action>".length)))
            } catch (e: Exception) {
                LOG.error("""Exception:  event.target = [${event.target}], event = [${event}], backtrack = [${backtrack}],
                        replacedValue = [${replacedValue}], factoid = [${factoid}]""", e)
            }
        } else {
            responses.add(Message(event, message))
        }
    }

    private fun validateTell(responses: MutableList<Message>, event: Message) {
        if (event.tell) {
            val channel = event.channel
            var targetUser = event.target!!
            if (targetUser.nick.equals(bot.getNick(), ignoreCase = true)) {
                responses.add(Message(event, Sofia.botSelfTalk()))
            } else {
                if (channel != null) {
                    if (!bot.isOnCommonChannel(targetUser)) {
                        responses.add(Message(event, Sofia.userNotInChannel(targetUser.nick, channel.name)))
                    } else if (event.user.nick == channel.name && !bot.isOnCommonChannel(targetUser)) {
                        responses.add(Message(event, Sofia.userNoSharedChannels()))
                    }
                }
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(GetFactoidOperation::class.java)
    }
}
