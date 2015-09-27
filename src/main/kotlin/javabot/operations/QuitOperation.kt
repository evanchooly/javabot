package javabot.operations

import javabot.Message
import javabot.dao.ConfigDao

import javax.inject.Inject

public class QuitOperation : BotOperation() {

    @Inject
    private val configDao: ConfigDao? = null

    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        if (message.toLowerCase().startsWith("quit ")) {
            if (message.substring("quit ".length()) == configDao!!.get().password) {
                System.exit(0)
            }
            return true
        }
        return false
    }
}