package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameters
import javabot.Javabot
import javabot.Message
import javabot.operations.BotOperation
import org.pircbotx.PircBotX
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Provider

@Parameters(separators = "=")
public abstract class AdminCommand : BotOperation() {

    protected var args: MutableList<Any?> = arrayListOf()

    @Inject
    lateinit var javabot: Provider<Javabot>

    @Inject
    lateinit var pircBot: Provider<PircBotX>

    override fun handleMessage(event: Message): Boolean {
        var handled = false
        var message = event.value
        if (message.toLowerCase().startsWith("admin ")) {
            if (isAdminUser(event.user)) {
                message = message.substring(6)
                val params = message.split(" ") as MutableList
                if (canHandle(params[0])) {
                    handled = true
                    try {
                        synchronized (this) {
                            parse(params)
                            execute(event)
                        }
                    } catch (e: Exception) {
                        LOG.error(e.getMessage(), e)
                        bot.postMessageToUser(event.user, Sofia.adminParseFailure(e.getMessage()!!))
                    }

                }
            } else {
                bot.postMessageToChannel(event, Sofia.notAdmin(event.user.nick))
                handled = true
            }
        }
        return handled
    }

    public open fun canHandle(message: String): Boolean {
        try {
            return message.equals(javaClass.simpleName, ignoreCase = true)
        } catch (e: Exception) {
            LOG.error(e.getMessage(), e)
            throw RuntimeException(e.getMessage(), e)
        }

    }

    public abstract fun execute(event: Message)

    @SuppressWarnings("unchecked")
    public fun parse(params: MutableList<String>) {
        try {

/*
            var index = 2
            while (index < params.size()) {
                if (!params.get(index).startsWith("-")) {
                    params.set(index - 1, params.get(index - 1) + " " + params.remove(index))
                } else {
                    index++
                }
            }
            if ("admin" == params.get(0)) {
                params.remove(0)
            }
            params.remove(0)
*/
            JCommander(this).parse(*params.filter { it.startsWith("-")}.toTypedArray())
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }


    public fun getCommandName(): String {
        var name = javaClass.simpleName
        name = name.substring(0, 1).toLowerCase() + name.substring(1)
        return name
    }

    override fun toString(): String {
        return "%s [admin]".format(getName())
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AdminCommand::class.java)
    }
}
