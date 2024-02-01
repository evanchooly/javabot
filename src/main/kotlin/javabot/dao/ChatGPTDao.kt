package javabot.dao

import com.google.inject.Inject
import gpt.api.GPT
import gpt.models.Message
import gpt.models.MessageModel
import javabot.JavabotConfig
import java.util.*


class ChatGPTDao
@Inject
constructor(val javabotConfig: JavabotConfig) {
    fun sendPromptToChatGPT(prompt: String): String? {
        println("chat gpt key: '${javabotConfig.chatGptKey()}'")
        return if (javabotConfig.chatGptKey().isNotEmpty()) {
            val model = "gpt-4"
            val gpt = GPT(javabotConfig.chatGptKey())
            val messageModel = MessageModel(model, listOf(Message("user", prompt)))
            val messageResponse = gpt.sendMessage(messageModel)
            return messageResponse.choices[0].message.content
        } else {
            // no chatGPT key? No chatGPT attempt.
            null
        }
    }

}
