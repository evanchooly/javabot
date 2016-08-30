package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.operations.locator.JCPJSRLocator
import javax.inject.Inject

class JSROperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var locator: JCPJSRLocator) : BotOperation(bot, adminDao) {

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value.toLowerCase()
        if ("jsr" == message.trim()) {
            responses.add(Message(event, Sofia.jsrMissing()))
        } else {
            if (message.startsWith("jsr ")) {
                val jsrString = message.substring("jsr ".length)

                try {
                    val jsr = Integer.parseInt(jsrString)
                    val response = locator.findInformation(jsr)
                    responses.add(Message(event, if (!response.isEmpty()) response else Sofia.jsrUnknown(jsrString)))
                } catch (nfe: NumberFormatException) {
                    responses.add(Message(event, Sofia.jsrInvalid(jsrString)))
                }
            }
        }

        return responses
    }
}

