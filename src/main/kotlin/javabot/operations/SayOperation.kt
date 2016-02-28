package javabot.operations

import javabot.Message

class SayOperation : BotOperation() {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if (message.startsWith("say ")) {
            responses.add(Message(event, message.substring("say ".length)))
        }
        return responses
    }
}