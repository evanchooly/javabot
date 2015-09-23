package javabot.commands

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.ConfigDao
import javabot.model.Config
import org.apache.commons.lang.StringUtils

import javax.inject.Inject
import java.lang.reflect.Method

public class Configure : AdminCommand() {
    Inject
    private val dao: ConfigDao? = null
    Param(required = false)
    var property: String
    Param(required = false)
    var value: String

    override fun execute(event: Message) {
        val config = dao!!.get()
        if (StringUtils.isEmpty(property)) {
            bot.postMessageToUser(event.user, config.toString())
        } else {
            try {
                val name = property.substring(0, 1).toUpperCase() + property.substring(1)
                val get = config.javaClass.getDeclaredMethod("get" + name)
                val type = get.returnType
                val set = config.javaClass.getDeclaredMethod("set" + name, type)
                try {
                    set.invoke(config, if (type == String::class.java) value.trim() else Integer.parseInt(value))
                    dao.save(config)
                    bot.postMessageToUser(event.user, Sofia.configurationSetProperty(property, value))
                } catch (e: ReflectiveOperationException) {
                    bot.postMessageToUser(event.user, e.getMessage())
                } catch (e: NumberFormatException) {
                    bot.postMessageToUser(event.user, e.getMessage())
                }

            } catch (e: NoSuchMethodException) {
                bot.postMessageToUser(event.user, Sofia.configurationUnknownProperty(property))
            }

        }
    }
}
