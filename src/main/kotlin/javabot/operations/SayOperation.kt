package javabot.operations

import javabot.Message

public class SayOperation : BotOperation() {
    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        if (message.startsWith("say ")) {
            bot.postMessageToChannel(event, message.substring("say ".length()))
            return true
        }
        return false
    }
}