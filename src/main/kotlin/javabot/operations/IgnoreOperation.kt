package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message

public class IgnoreOperation : BotOperation() {
    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        if (message.startsWith("ignore ")) {
            val parts = message.split(" ")
            bot.addIgnore(parts[1])
            bot.postMessageToChannel(event, Sofia.botIgnoring(parts[1]))
            return true
        }
        return false
    }
}
