package javabot.operations

import com.google.inject.Inject
import java.util.Locale
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao

class Magic8BallOperation @Inject constructor(bot: Javabot, adminDao: AdminDao) :
    BotOperation(bot, adminDao) {
    val entries =
        listOf(
            "Yes",
            "Definitely",
            "Absolutely",
            "I think so",
            "I guess that would be good",
            "I'm not really sure",
            "If you want",
            "Well, if you really want to",
            "Maybe",
            "Probably not",
            "Not really",
            "Sometimes",
            "Hmm, sounds bad",
            "No chance!",
            "No way!",
            "No",
            "Absolutely not!",
            "Definitely not!",
            "Don't do anything I wouldn't do",
            "Only on a Tuesday",
            "If I tell you that I'll have to shoot you",
            "I'm getting something about JFK, but I don't think it's relevant",
        )

    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value.lowercase(Locale.getDefault())
        if (message.startsWith("should i ") || message.startsWith("magic8 ")) {
            responses.add(Message(event, entries[((Math.random() * entries.size).toInt())]))
        }
        return responses
    }
}
