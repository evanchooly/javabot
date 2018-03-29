package javabot.operations

import com.google.inject.Inject
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao

class SayOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) : BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if (message.startsWith("say ")) {
            // trims out a response if the channel name isn't empty AND the user isn't an op
            if (event.channel == null || (bot.adapter.isOp(event.user.nick, event.channel.name))) {
                responses.add(Message(event, message.substring("say ".length)))
            }
        }
        return responses
    }
}