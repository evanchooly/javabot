package javabot.commands

import com.antwerkz.sofia.Sofia
import com.beust.jcommander.Parameter
import jakarta.inject.Inject
import java.util.Locale
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ConfigDao

class Configure @Inject constructor(bot: Javabot, adminDao: AdminDao, var configDao: ConfigDao) :
    AdminCommand(bot, adminDao) {

    @Parameter(names = arrayOf("--property")) lateinit var property: String
    @Parameter(names = arrayOf("--value")) lateinit var value: String

    override fun execute(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val config = configDao.get()
        if (!this::property.isInitialized || !this::value.isInitialized) {
            responses.add(Message(event.user, config.toString()))
        } else {
            try {
                val name =
                    property.substring(0, 1).uppercase(Locale.getDefault()) + property.substring(1)
                val get = config.javaClass.getDeclaredMethod("get" + name)
                val type = get.returnType
                val set = config.javaClass.getDeclaredMethod("set" + name, type)
                try {
                    set.invoke(
                        config,
                        if (type == String::class.java) value.trim() else Integer.parseInt(value)
                    )
                    configDao.save(config)
                    responses.add(
                        Message(event.user, Sofia.configurationSetProperty(property, value))
                    )
                } catch (e: ReflectiveOperationException) {
                    responses.add(Message(event.user, e.message!!))
                } catch (e: NumberFormatException) {
                    responses.add(Message(event.user, e.message!!))
                }
            } catch (e: NoSuchMethodException) {
                responses.add(Message(event.user, Sofia.configurationUnknownProperty(property)))
            }
        }
        return responses
    }
}
