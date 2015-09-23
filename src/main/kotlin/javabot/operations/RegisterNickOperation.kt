package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ConfigDao
import javabot.model.Config
import javabot.model.NickRegistration

import javax.inject.Inject

public class RegisterNickOperation : BotOperation() {
    Inject
    private val adminDao: AdminDao? = null
    Inject
    private val configDao: ConfigDao? = null

    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        if (message.startsWith("register ")) {
            val split = message.split(" ")
            if (split.size() > 1) {
                val twitterName = split[1]
                val registration = NickRegistration(event.user, twitterName)
                adminDao!!.save(registration)
                val config = configDao!!.get()
                val eventMessage = Sofia.registerNick(config.url, registration.url, twitterName)

                bot.postMessageToUser(event.user, eventMessage)
                return true
            }
        }
        return false
    }
}
