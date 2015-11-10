package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.operations.locator.JCPJSRLocator
import javax.inject.Inject

public class JSROperation : BotOperation() {
    @Inject
    lateinit var locator: JCPJSRLocator

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value.toLowerCase()
        if ("jsr" == message) {
            responses.add(Message(event, Sofia.jsrMissing()))
        } else {
            if (message.startsWith("jsr ")) {
                val jsrString = message.substring("jsr ".length)

                try {
                    val jsr = Integer.parseInt(jsrString)
                    val response = locator.findInformation(jsr)
                    if (!response.isEmpty()) {
                        responses.add(Message(event, response))
                    } else {
                        responses.add(Message(event, Sofia.jsrUnknown(jsrString)))
                    }
                } catch (nfe: NumberFormatException) {
                    responses.add(Message(event, Sofia.jsrInvalid(jsrString)))
                }
            }
        }

        return responses
    }
}

