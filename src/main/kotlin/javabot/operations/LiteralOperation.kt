package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.FactoidDao
import javax.inject.Inject

public class LiteralOperation : BotOperation() {
    @Inject
    lateinit var dao: FactoidDao

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value.toLowerCase()
        if (message.startsWith("literal ")) {
            val key = message.substring("literal ".length)
            val factoid = dao.getFactoid(key)
            responses.add(Message(event, if (factoid != null) factoid.value else Sofia.factoidUnknown(key)))
        }
        return responses
    }
}
