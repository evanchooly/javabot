package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message

class IgnoreOperation : BotOperation() {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if (message.startsWith("ignore ")) {
            val parts = message.split(" ")
            bot.addIgnore(parts[1])
            responses.add(Message(event, Sofia.botIgnoring(parts[1])))
        }
        return responses
    }
}
