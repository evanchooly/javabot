package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.operations.BotOperation
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import org.apache.commons.lang.StringUtils
import org.pircbotx.PircBotX
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject
import javax.inject.Provider
import java.lang.reflect.Field
import java.util.ArrayList
import java.util.Arrays

public abstract class AdminCommand : BotOperation() {

    protected var args: MutableList<String>

    Inject
    private val javabot: Provider<Javabot>? = null

    Inject
    private val pircBot: Provider<PircBotX>? = null

    public fun getJavabot(): Javabot {
        return javabot!!.get()
    }

    public fun getIrcBot(): PircBotX {
        return pircBot!!.get()
    }

    override fun handleMessage(event: Message): Boolean {
        var handled = false
        var message = event.value
        if (message.toLowerCase().startsWith("admin ")) {
            if (isAdminUser(event.user)) {
                message = message.substring(6)
                val split = message.split(" ")
                if (canHandle(split[0])) {
                    handled = true
                    try {
                        synchronized (this) {
                            val params = ArrayList(Arrays.asList<String>(*split))
                            parse(params)
                            execute(event)
                            clear()
                        }
                    } catch (e: ParseException) {
                        LOG.error(e.getMessage(), e)
                        bot.postMessageToUser(event.user, Sofia.adminParseFailure(e.getMessage()))
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
            return message.equalsIgnoreCase(javaClass.simpleName)
        } catch (e: Exception) {
            LOG.error(e.getMessage(), e)
            throw RuntimeException(e.getMessage(), e)
        }

    }

    public abstract fun execute(event: Message)

    public fun getOptions(): Options {
        val options = Options()
        for (field in javaClass.declaredFields) {
            val annotation = field.getAnnotation(Param::class.java)
            if (annotation != null) {
                val name = if ("" == annotation.name()) field.name else annotation.name()
                val value = if (!StringUtils.isEmpty(annotation.defaultValue())) annotation.defaultValue() else null
                val option = object : Option(name, true, null) {
                    override fun getValue(): String {
                        val optValue = super.getValue()
                        return optValue ?: value
                    }
                }
                option.isRequired = annotation.required() && !annotation.primary()
                options.addOption(option)
            }
        }
        return options
    }

    SuppressWarnings("unchecked")
    Throws(ParseException::class)
    public fun parse(params: MutableList<String>) {
        var index = 2
        while (index < params.size()) {
            if (!params.get(index).startsWith("-")) {
                params.set(index - 1, params.get(index - 1) + " " + params.remove(index))
            } else {
                index++
            }
        }
        val options = getOptions()
        val parser = GnuParser()
        val line = parser.parse(options, params.toArray<String>(arrayOfNulls<String>(params.size())))
        args = line.argList
        try {
            val iterator = line.iterator()
            while (iterator.hasNext()) {
                val option = iterator.next() as Option
                val field = javaClass.getDeclaredField(option.opt)
                field.set(this, option.value)
            }
            if ("admin" == args.get(0)) {
                args.remove(0)
            }
            if (args.size() == 2) {
                val field = getPrimaryParam() ?: throw ParseException("Required option missing from " + getCommandName())
                field.set(this, args.get(1))
            } else if (args.size() > 2) {
                throw ParseException("Too many options given to " + getCommandName())
            }
        } catch (e: NoSuchFieldException) {
            LOG.error(e.getMessage(), e)
            throw ParseException(e.getMessage())
        } catch (e: IllegalAccessException) {
            LOG.error(e.getMessage(), e)
            throw ParseException(e.getMessage())
        }

    }

    Throws(ParseException::class)
    private fun clear() {
        try {
            for (o in getOptions().options) {
                val field = javaClass.getDeclaredField((o as Option).opt)
                field.set(this, null)
            }
        } catch (e: NoSuchFieldException) {
            LOG.error(e.getMessage(), e)
            throw ParseException(e.getMessage())
        } catch (e: IllegalAccessException) {
            LOG.error(e.getMessage(), e)
            throw ParseException(e.getMessage())
        }

    }


    private fun getPrimaryParam(): Field? {
        for (field in javaClass.declaredFields) {
            val annotation = field.getAnnotation(Param::class.java)
            if (annotation != null && annotation.primary()) {
                return field
            }
        }
        return null
    }

    public fun getCommandName(): String {
        var name = javaClass.simpleName
        name = name.substring(0, 1).toLowerCase() + name.substring(1)
        return name
    }

    override fun toString(): String {
        return String.format("%s [admin]", name)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AdminCommand::class.java)
    }
}
