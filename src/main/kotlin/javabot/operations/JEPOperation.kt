package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.operations.locator.JEPLocator
import javax.inject.Inject

/**
 * Astute coders might notice a SLIGHT similarity to JSROperation... as in, it's copied and pasted directly
 * with references to JSR stuff being renamed to JEP stuff instead.
 */
class JEPOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var locator: JEPLocator) : BotOperation(bot, adminDao) {

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value.toLowerCase()
        if ("jep" == message.trim()) {
            responses.add(Message(event, Sofia.jepMissing()))
        } else {
            if (message.startsWith("jep ")) {
                val jepString = message.substring("jep ".length)

                try {
                    val jep = Integer.parseInt(jepString)
                    val response = locator.findInformation(jep)
                    if (!response.isEmpty()) {
                        responses.add(Message(event, response))
                    } else {
                        responses.add(Message(event, Sofia.jepUnknown(jepString)))
                    }
                } catch (nfe: NumberFormatException) {
                    responses.add(Message(event, Sofia.jepInvalid(jepString)))
                }
            }
        }

        return responses
    }
}

