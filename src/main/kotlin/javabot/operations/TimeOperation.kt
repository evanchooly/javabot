package javabot.operations

import javabot.Message

import java.util.Calendar

public class TimeOperation : BotOperation() {
    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        if ("time" == message || "date" == message) {
            bot.postMessageToChannel(event, Calendar.getInstance().time.toString())
            return true
        }
        return false
    }
}
