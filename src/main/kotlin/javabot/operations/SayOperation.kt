package javabot.operations

import com.google.inject.Inject
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao

class SayOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val message = if (event.value.startsWith("~")) event.value.substring(1) else event.value
        return if (message.startsWith("say ")) {
            if (isPrivateMessage(event)) {
                val tokens = message.substring("say ".length).split(" ").toMutableList()
                if (tokens.size > 2 && isChannelName(tokens[0])) {
                    val channelName = tokens[0]
                    if(isOp(event, channelName)) {
                        // need to rebuild the message less channel name
                        val reconstructedMessage=tokens.takeLast(tokens.size-1).joinToString(" ")
                        listOf(Message(bot.channelDao.get(channelName)!!, event, reconstructedMessage))
                    } else {
                        emptyList()
                    }
                } else emptyList()
            } else {
                listOf(Message(event, message.substring("say ".length)))
            }
        } else  emptyList()
    }

    private fun isPrivateMessage(event: Message): Boolean {
        return (event.channel == null)
    }
}