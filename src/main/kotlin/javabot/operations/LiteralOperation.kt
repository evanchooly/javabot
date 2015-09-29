package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.FactoidDao
import javabot.model.Factoid
import org.pircbotx.Channel

import javax.inject.Inject

public class LiteralOperation : BotOperation() {
    @Inject
    lateinit var dao: FactoidDao

    /**
     * @see BotOperation.handleMessage
     */
    override fun handleMessage(event: Message): Boolean {
        val message = event.value.toLowerCase()
        if (message.startsWith("literal ")) {
            val key = message.substring("literal ".length())
            val factoid = dao.getFactoid(key)
            bot.postMessageToChannel(event, if (factoid != null) factoid.value else Sofia.factoidUnknown(key))
            return true
        }
        return false
    }
}
