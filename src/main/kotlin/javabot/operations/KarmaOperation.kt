package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.KarmaDao
import javabot.model.Karma
import org.slf4j.LoggerFactory
import java.util.regex.Pattern
import javax.inject.Inject

public class KarmaOperation : BotOperation() {

    @Inject
    lateinit var dao: KarmaDao

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        if(event.tell) {
            responses.add(Message(event, Sofia.notAllowed()))
            return responses;
        }

        readKarma(responses, event)
        if (responses.isEmpty()) {
            val message = event.value
            val sender = event.user
            val channel = event.channel
            var operationPointer = message.indexOf("++")
            var increment = true
            if (operationPointer == -1) {
                operationPointer = message.indexOf("--")
                increment = false
                // check for no karma inc/dec, and ~-- and ~++ too
                if (operationPointer < 1) {
                    return responses
                }
            }

            /*
             * we won't get here unless operationPointer>0.
             *
             * But things get wonky; we need to handle two alternatives if it's a
             * karma decrement. One is: "admin --name=foo" and the other is
             * "foo --". We may need to apply a regex to ascertain whether
             * the signal is an option or not.
             *
             * The regex assumes options look like "--foo="
             */
            if (!increment) {
                val potentialParam = message.substring(operationPointer - 1)
                if (optionPattern.matcher(potentialParam).find()) {
                    // we PRESUMABLY have an option...
                    return responses
                }
            }
            if (operationPointer != message.length - 2 && message[operationPointer + 2] != ' ') {
                return responses
            }
            val nick: String
            try {
                nick = message.substring(0, operationPointer).trim().toLowerCase()
            } catch (e: StringIndexOutOfBoundsException) {
                log.info("message = " + message, e)
                throw e
            }

            // got an empty nick; spaces only?
            if (!nick.isEmpty()) {
                if (channel == null || !channel.name.startsWith("#")) {
                    responses.add(Message(event, Sofia.privmsgChange()))
                } else {
                    if (nick.equals(sender.nick, ignoreCase = true)) {
                        if (increment) {
                            responses.add(Message(event, Sofia.karmaOwnIncrement()))
                        }
                        increment = false
                    }
                    var karma: Karma? = dao.find(nick)
                    if (karma == null) {
                        karma = Karma()
                        karma.name = nick
                    }
                    if (increment) {
                        karma.value = karma.value + 1
                    } else {
                        karma.value = karma.value - 1
                    }
                    karma.userName = sender.nick
                    dao.save(karma)
                    readKarma(responses, Message(event.channel, event.user, "karma " + nick))
                }
            }
        }
        return responses
    }

    public fun readKarma(responses: MutableList<Message>, event: Message) {
        val message = event.value
        val sender = event.user
        if (message.startsWith("karma ")) {
            val nick = message.substring("karma ".length).toLowerCase()
            val karma = dao.find(nick)
            if (karma != null) {
                responses.add(Message(event, if (nick.equals(sender.nick, ignoreCase = true))
                    Sofia.karmaOwnValue(sender.nick, karma.value)
                else
                    Sofia.karmaOthersValue(nick, karma.value, sender.nick)))
            } else {
                responses.add(Message(event, if (sender.nick == nick)
                    Sofia.karmaOwnNone(sender.nick)
                else
                    Sofia.karmaOthersNone(nick, sender.nick)))
            }

        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(KarmaOperation::class.java)

        private val optionPattern = Pattern.compile("\\s--\\p{Alpha}[\\p{Alnum}]*=")
    }

}