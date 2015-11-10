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

@Parameters(separators = "=", optionPrefixes = "--")
public abstract class AdminCommand : BotOperation() {

    @Inject
    lateinit var javabot: Provider<Javabot>

    @Inject
    lateinit var pircBot: Provider<PircBotX>

    override fun handleMessage(event: Message): List<Message> {
        var responses = arrayListOf<Message>()
        var message = event.value
        if (message.toLowerCase().startsWith("admin ")) {
            if (isAdminUser(event.user)) {
                message = message.substring(6)
                val params = message.split(" ") as MutableList
                if (canHandle(params[0])) {
                    try {
                        synchronized (this) {
                            parse(params)
                            responses.addAll(execute(event))
                        }
                    } catch (e: Exception) {
                        LOG.error(e.message, e)
                        responses.add(Message(event.user, Sofia.adminParseFailure(e.message!!)))
                    }

                }
            } else {
                responses.add(Message(event, Sofia.notAdmin(event.user.nick)))
            }
        }
        return responses
    }

    protected open fun parse(params: MutableList<String>) {
        JCommander(this).parse(*params.subList(1, params.size).toTypedArray())
    }

    public open fun canHandle(message: String): Boolean {
        try {
            return message.equals(javaClass.simpleName, ignoreCase = true)
        } catch (e: Exception) {
            LOG.error(e.message, e)
            throw RuntimeException(e.message, e)
        }

    }

    public abstract fun execute(event: Message) : List<Message>

    public fun getCommandName(): String {
        var name = javaClass.simpleName
        name = name.substring(0, 1).toLowerCase() + name.substring(1)
        return name
    }

    override fun toString(): String {
        return "${getName()} [admin]"
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AdminCommand::class.java)
    }
}
