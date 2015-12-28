package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ChangeDao
import javabot.dao.FactoidDao
import javabot.dao.LogsDao
import javabot.model.Factoid
import javabot.model.Logs.Type
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject

public class AddFactoidOperation : BotOperation(), StandardOperation {
    companion object {
        public val log: Logger = LoggerFactory.getLogger(AddFactoidOperation::class.java)
    }

    @Inject
    lateinit var factoidDao: FactoidDao
    @Inject
    lateinit var logDao: LogsDao
    @Inject
    lateinit var changeDao: ChangeDao

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
                var factoid: Factoid?

                if (key.startsWith("no ") || key.startsWith("no, ")) {
                    key = key.substring(key.indexOf(" ")).trim()

                    factoid = factoidDao.getFactoid(key)
                    if (factoid != null && factoid.locked && !admin) {
                        changeDao.logChange(Sofia.changingLockedFactoid(event.user.nick, key))
                        responses.add(Message(event, Sofia.factoidLocked(event.user.nick)))
                    }
                } else {
                    if (key.startsWith(",")) {
                        key = message.substring(1)
                    }
                    factoid = Factoid(key.trim().toLowerCase(), userName = event.user.nick)
                    factoid.name = factoid.name.dropLastWhile { it in arrayOf('.', '?', '!') }
                }
                if ( factoid == null) {
                    responses.add(Message(event.channel, event.user, Sofia.factoidUnknown(key)))
                } else if (factoid.name.isEmpty()) {
                    responses.add(Message(event, Sofia.factoidInvalidName()))
                } else if (message.isEmpty()) {
                    responses.add(Message(event, Sofia.factoidInvalidValue()))
                } else if (factoid.value.startsWith("<see>")) {
                    factoid.value = factoid.value.toLowerCase()
                } else {
                    factoid.value = message
                    if (factoid.id != null) {
                        changeDao.logChange(Sofia.factoidChanged(event.user.nick, factoid.name, factoid.value, message,
                                event.channel?.name ?: "private message"))
                    } else {
                        if (factoidDao.hasFactoid(factoid.name)) {
                            responses.add(Message(event, Sofia.factoidExists(factoid.name, event.user.nick)))
                        } else {
                            changeDao.logAdd(event.user.nick, factoid.name, factoid.value)
                        }
                    }
                    if (responses.isEmpty()) {
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
