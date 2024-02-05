package javabot.operations

import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.ChatGPTDao
import javabot.operations.throttle.BotRateLimiter
import java.util.*
import javax.inject.Inject

/** Gets current weather conditions for a place given as a parameter. */
class ChatGPTOperation
@Inject
constructor(bot: Javabot, adminDao: AdminDao, var chatGPTDao: ChatGPTDao) :
    BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val responses = mutableListOf<Message>()
        val message = event.value
        if (message.lowercase(Locale.getDefault()).startsWith("gpt ")) {
            val uuid= UUID.randomUUID()
            val query = message.substringAfter("gpt ")
            val prompt = """Someone is asking "$query". 
                Restrict your answer to being applicable to the Java Virtual Machine, 
                and limit the response's length as if it were to be posted on Twitter, 
                but without hashtags or other such twitter-like features; 
                if the answer does not contain constructive information for Java programming, 
                respond ONLY with \"$uuid-not applicable\" and no other text""".trimIndent()
            val result = chatGPTDao.sendPromptToChatGPT(prompt)
            if (!result.isNullOrEmpty() && !result.lowercase().contains(uuid.toString())) {
                responses.add(Message(event, result.toString()))
            }
        }
        return responses
    }
}


