package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.FactoidDao
import javabot.dao.LogsDao
import javabot.model.Factoid
import javabot.model.Logs.Type
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import javax.inject.Inject

public class AddFactoidOperation : BotOperation(), StandardOperation {

    @Inject
    lateinit var factoidDao: FactoidDao

    @Inject
    lateinit var logDao: LogsDao

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        var message = event.value
        val channel = event.channel
        if (message.startsWith("no ") || message.startsWith("no, ")) {
            if ((channel == null || !channel.name.startsWith("#")) && !isAdminUser(event.user)) {
                responses.add(Message(event, Sofia.privmsgChange()))
            } else {
                message = message.substring(2)
                if (message.startsWith(",")) {
                    message = message.substring(1)
                }
                message = message.trim()
                updateFactoid(responses, event, message)
            }
        }
        if (responses.isEmpty()) {
            addFactoid(responses, event, message)
        }
        return responses
    }

    private fun updateFactoid(responses: MutableList<Message>, event: Message, message: String) {
        val `is` = message.indexOf(" is ")
        if (`is` != -1) {
            val channel = event.channel
            if ((channel == null || !channel.name.startsWith("#")) && !isAdminUser(event.user)) {
                responses.add(Message(event, Sofia.privmsgChange()))
            } else {
                var key = message.substring(0, `is`)
                key = key.replace("^\\s+".toRegex(), "")
                val factoid = factoidDao.getFactoid(key)
                val admin = isAdminUser(event.user)
                if (factoid != null) {
                    if (factoid.locked) {
                        if (admin) {
                            updateExistingFactoid(responses, event, message, factoid, `is`, key)
                        } else {
                            logDao.logMessage(Type.MESSAGE, event.channel, event.user,
                                    Sofia.changingLockedFactoid(event.user.nick, key))
                            responses.add(Message(event, Sofia.factoidLocked(event.user.nick)))
                        }
                    } else {
                        updateExistingFactoid(responses, event, message, factoid, `is`, key)
                    }
                }
            }
        }
    }

    private fun addFactoid(responses: MutableList<Message>, event: Message, message: String): Boolean {
        var handled = false
        if (message.toLowerCase().contains(" is ")) {
            if ((event.channel == null || !event.channel.name.startsWith("#")) && !isAdminUser(event.user)) {
                responses.add(Message(event, Sofia.privmsgChange()))
            } else {
                var key = message.substring(0, message.indexOf(" is "))
                key = key.toLowerCase()
                while (key.endsWith(".") || key.endsWith("?") || key.endsWith("!")) {
                    key = key.substring(0, key.length - 1)
                }
                val index = message.indexOf(" is ")
                var value: String? = null
                if (index != -1) {
                    value = message.substring(index + 4, message.length)
                }
                if (key.trim().isEmpty()) {
                    responses.add(Message(event, Sofia.factoidInvalidName()))
                } else if (value == null || value.trim().isEmpty()) {
                    responses.add(Message(event, Sofia.factoidInvalidValue()))
                } else if (factoidDao.hasFactoid(key)) {
                    if (event.channel != null) {
                        responses.add(Message(event, Sofia.factoidExists(key, event.user.nick)))
                    } else {
                        responses.add(Message(event.user, Sofia.factoidExists(key, event.user.nick)))
                    }
                    handled = true
                } else {
                    if (value.startsWith("<see>")) {
                        value = value.toLowerCase()
                    }
                    factoidDao.addFactoid(event.user.nick, key, value)
                    if (event.channel != null) {
                        responses.add(Message(event, Sofia.ok(event.user.nick)))
                    } else {
                        responses.add(Message(event.user, Sofia.ok(event.user.nick)))
                    }
                    handled = true
                }
            }
        }
        return handled
    }

    private fun updateExistingFactoid(responses: MutableList<Message>, event: Message, message: String, factoid: Factoid,
                                      `is`: Int, key: String) {
        val newValue = message.substring(`is` + 4)
        logDao.logMessage(Type.MESSAGE, event.channel, event.user,
                Sofia.factoidChanged(event.user.nick, key, factoid.value, newValue, event.channel!!.name))
        factoid.value = newValue
        factoid.updated = LocalDateTime.now()
        factoid.userName = event.user.nick
        factoidDao.save(factoid)
        responses.add(Message(event, Sofia.ok(event.user.nick)))
    }

    /**
     * Adding factoids should happen after everything else has had a chance to run. See issue #88.
     */
    override fun getPriority(): Int {
        return 1
    }

    companion object {
        public val log: Logger = LoggerFactory.getLogger(AddFactoidOperation::class.java)
    }

}
