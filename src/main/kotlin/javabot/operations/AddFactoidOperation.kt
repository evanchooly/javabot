package javabot.operations

import com.antwerkz.sofia.Sofia
import java.time.LocalDateTime
import java.util.Locale
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChangeDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javabot.model.Factoid
import javax.inject.Inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AddFactoidOperation
@Inject
constructor(
    bot: Javabot,
    adminDao: AdminDao,
    var factoidDao: FactoidDao,
    var changeDao: ChangeDao,
    var channelDao: ChannelDao,
) : BotOperation(bot, adminDao), StandardOperation {

    companion object {
        val log: Logger = LoggerFactory.getLogger(AddFactoidOperation::class.java)
    }

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        var message = event.value
        val channel = event.channel
        val admin = isAdminUser(event.user)
        val index = message.indexOf(" is ")
        if (index != -1) {
            if ((channel == null || !channel.name.startsWith("#")) && !admin) {
                responses.add(Message(event, Sofia.privmsgChange()))
            } else {
                var key = message.substring(0, index)
                message = message.substring(index + 4).trim()
                var factoid: Factoid? = null

                val redefine = key.startsWith("no ", true) || key.startsWith("no, ", true)
                var exists = false
                if (redefine) {
                    key = key.substring(key.indexOf(" ")).trim().lowercase(Locale.getDefault())

                    factoid = factoidDao.getFactoid(key)
                    if (factoid == null) {
                        responses.add(Message(event.channel, event.user, Sofia.factoidUnknown(key)))
                    } else {
                        exists = true
                    }
                } else {
                    if (key.startsWith(",")) {
                        key = message.substring(1)
                    }
                    val name = key.trim().lowercase(Locale.getDefault())
                    if (name.isEmpty()) {
                        responses.add(Message(event, Sofia.factoidInvalidName()))
                    }
                    if (message.isEmpty()) {
                        responses.add(Message(event, Sofia.factoidInvalidValue()))
                    } else {
                        factoid = factoidDao.getFactoid(key)
                        if (factoid != null) {
                            responses.add(
                                Message(event, Sofia.factoidExists(factoid.name, event.user.nick))
                            )
                            exists = true
                        } else {
                            factoid = Factoid(name, userName = event.user.nick)
                            factoid.name =
                                factoid.name.dropLastWhile { it in arrayOf('.', '?', '!') }
                        }
                    }
                }
                if (factoid != null) {
                    if (exists && !redefine) {
                        if (factoid.locked && !admin) {
                            changeDao.logChangingLockedFactoid(
                                event.user.nick,
                                key,
                                channel?.name ?: "private message",
                            )
                            responses.add(Message(event, Sofia.factoidLocked(event.user.nick)))
                        }
                    } else {
                        factoid.value = message
                        if (factoid.value.startsWith("<see>")) {
                            factoid.value = factoid.value.lowercase(Locale.getDefault())
                        }
                        if (redefine) {
                            factoid.userName = event.user.nick
                            factoid.updated = LocalDateTime.now()
                        }
                        if (factoid.id != null) {
                            changeDao.logFactoidChanged(
                                event.user.nick,
                                factoid.name,
                                factoid.value,
                                message,
                                channelDao.location(channel),
                            )
                        } else {
                            changeDao.logFactoidAdded(
                                event.user.nick,
                                factoid.name,
                                factoid.value,
                                channelDao.location(channel),
                            )
                        }
                        factoidDao.save(factoid)
                        responses.add(Message(event, Sofia.ok(event.user.nick)))
                    }
                }
            }
        }
        return responses
    }

    /**
     * Adding factoids should happen after everything else has had a chance to run. See issue #88.
     */
    override fun getPriority(): Int {
        return 1
    }
}
