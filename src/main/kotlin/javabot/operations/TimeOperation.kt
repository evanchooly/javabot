package javabot.operations

import javabot.Message

import java.util.Calendar

class TimeOperation : BotOperation() {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if ("time" == message || "date" == message) {
            responses.add(Message(event, Calendar.getInstance().time.toString()))
        }
        return responses
    }
}
