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

    override fun handleMessage(event: Message): Boolean {
        var message = event.value
        val channel = event.channel
        var handled = false
        if (message.startsWith("no ") || message.startsWith("no, ")) {
            if ((channel == null || !channel.name.startsWith("#")) && !isAdminUser(event.user)) {
                bot.postMessageToChannel(event, Sofia.privmsgChange())
                handled = true
            } else {
                message = message.substring(2)
                if (message.startsWith(",")) {
                    message = message.substring(1)
                }
                message = message.trim()
                handled = updateFactoid(event, message)
            }
        }
        if (!handled) {
            handled = addFactoid(event, message)
        }
        return handled
    }

    private fun updateFactoid(event: Message, message: String): Boolean {
        val `is` = message.indexOf(" is ")
        var handled = false
        if (`is` != -1) {
            val channel = event.channel
            if ((channel == null || !channel.name.startsWith("#")) && !isAdminUser(event.user)) {
                bot.postMessageToChannel(event, Sofia.privmsgChange())
            } else {
                var key = message.substring(0, `is`)
                key = key.replace("^\\s+".toRegex(), "")
                val factoid = factoidDao.getFactoid(key)
                val admin = isAdminUser(event.user)
                if (factoid != null) {
                    if (factoid.locked) {
                        if (admin) {
                            handled = updateExistingFactoid(event, message, factoid, `is`, key)
                        } else {
                            logDao.logMessage(Type.MESSAGE, event.channel, event.user,
                                  Sofia.changingLockedFactoid(event.user.nick, key))
                            bot.postMessageToChannel(event, Sofia.factoidLocked(event.user.nick))
                            handled = true
                        }
                    } else {
                        handled = updateExistingFactoid(event, message, factoid, `is`, key)
                    }
                }
            }
        }
        return handled
    }

    private fun addFactoid(event: Message, message: String): Boolean {
        var handled = false
        if (message.toLowerCase().contains(" is ")) {
            if ((event.channel == null || !event.channel.name.startsWith("#")) && !isAdminUser(event.user)) {
                bot.postMessageToChannel(event, Sofia.privmsgChange())
                handled = true
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
                    bot.postMessageToChannel(event, Sofia.factoidInvalidName())
                    handled = true
                } else if (value == null || value.trim().isEmpty()) {
                    bot.postMessageToChannel(event, Sofia.factoidInvalidValue())
                    handled = true
                } else if (factoidDao.hasFactoid(key)) {
                    if (event.channel != null) {
                        bot.postMessageToChannel(event, Sofia.factoidExists(key, event.user.nick))
                    } else {
                        bot.postMessageToUser(event.user, Sofia.factoidExists(key, event.user.nick))
                    }
                    handled = true
                } else {
                    if (value.startsWith("<see>")) {
                        value = value.toLowerCase()
                    }
                    factoidDao.addFactoid(event.user.nick, key, value)
                    if (event.channel != null) {
                        bot.postMessageToChannel(event, Sofia.ok(event.user.nick))
                    } else {
                        bot.postMessageToUser(event.user, Sofia.ok(event.user.nick))
                    }
                    handled = true
                }
            }
        }
        return handled
    }

    private fun updateExistingFactoid(event: Message, message: String, factoid: Factoid,
                                      `is`: Int, key: String): Boolean {
        val newValue = message.substring(`is` + 4)
        logDao.logMessage(Type.MESSAGE, event.channel, event.user,
              Sofia.factoidChanged(event.user.nick, key, factoid.value, newValue, event.channel!!.name))
        factoid.value = newValue
        factoid.updated = LocalDateTime.now()
        factoid.userName = event.user.nick
        factoidDao.save(factoid)
        bot.postMessageToChannel(event, Sofia.ok(event.user.nick))
        return true
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
