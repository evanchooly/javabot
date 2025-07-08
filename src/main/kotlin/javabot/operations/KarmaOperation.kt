package javabot.operations

import com.antwerkz.sofia.Sofia
import java.util.Locale
import java.util.regex.Pattern
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChangeDao
import javabot.dao.ChannelDao
import javabot.dao.KarmaDao
import javabot.model.Karma
import javax.inject.Inject

class KarmaOperation
@Inject
constructor(
    bot: Javabot,
    adminDao: AdminDao,
    var dao: KarmaDao,
    var changeDao: ChangeDao,
    var channelDao: ChannelDao,
) : BotOperation(bot, adminDao) {

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()

        readKarma(responses, event)
        if (responses.isEmpty()) {
            // if we're being addressed, we need to reconstruct the message so it looks like a
            // "karma operation"
            val message =
                if (event.addressed && !event.value.startsWith(bot.nick, true)) {
                    // argh. What if we're addressed, but it's not OUR KARMA being adjusted?
                    // We need to do some pre-emptive parsing to see if there's a match: if not,
                    // we have an "us++" situation.
                    val matches = operationPattern.matcher(event.value)
                    if (!matches.find()) {
                        "${bot.nick}${event.value}"
                    } else {
                        event.value
                    }
                } else {
                    event.value
                }
            val sender = event.user
            val channel = event.channel
            val matches = operationPattern.matcher(message)
            if (!matches.find()) {
                return responses
            }
            val tempSubject = matches.group(1)
            // we strip off the "completion character" if it's there
            val subject = tempSubject.substringBefore(":", tempSubject).trim()
            val operation = matches.group(2)
            var increment = operation.equals("++")

            // poison pill subject. Trust me on this one, people.
            if (subject.hashCode() == -1215158239) {
                return responses
            }

            if (event.tell) {
                responses.add(Message(event, Sofia.notAllowed()))
                return responses
            }

            // TODO handle tell (when there's an operation but no addressed nick: can we get here?)
            if (channel == null || !channel.name.startsWith("#")) {
                responses.add(Message(event, Sofia.privmsgChange()))
                return responses
            }
            if (increment && subject.equals(sender.nick, ignoreCase = true)) {
                responses.add(Message(event, Sofia.karmaOwnIncrement()))
                increment = false
            }
            val karma = dao.find(subject) ?: Karma(subject, 0, sender.nick)
            karma.value +=
                if (increment) {
                    1
                } else {
                    -1
                }
            dao.save(karma)
            changeDao.logKarmaChanged(
                karma.userName,
                karma.name,
                karma.value,
                channelDao.location(channel),
            )
            readKarma(responses, Message(event.channel, event.user, "karma $subject"))
        }
        return responses
    }

    private fun readKarma(responses: MutableList<Message>, event: Message) {
        val message = event.value
        val sender = event.user
        if (message.startsWith("karma ")) {
            val nick = message.substring("karma ".length).lowercase(Locale.getDefault())
            val karma = dao.find(nick)
            if (karma != null) {
                responses.add(
                    Message(
                        event,
                        if (nick.equals(sender.nick, ignoreCase = true))
                            Sofia.karmaOwnValue(sender.nick, karma.value)
                        else Sofia.karmaOthersValue(nick, karma.value, sender.nick),
                    )
                )
            } else {
                responses.add(
                    Message(
                        event,
                        if (sender.nick == nick) Sofia.karmaOwnNone(sender.nick)
                        else Sofia.karmaOthersNone(nick, sender.nick),
                    )
                )
            }
        }
    }

    companion object {
        private val operationPattern =
            Pattern.compile("^(?<nick>.+)(?<operation>\\+{2}|--).*\$", Pattern.COMMENTS)
    }
}
