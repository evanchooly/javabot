package javabot.dao

import com.google.inject.Inject
import com.google.inject.Singleton
import gpt.api.GPT
import gpt.models.Message
import gpt.models.MessageModel
import io.dropwizard.util.Duration
import javabot.JavabotConfig
import javabot.operations.throttle.BotRateLimiter

@Singleton
class ChatGPTDao @Inject constructor(val javabotConfig: JavabotConfig) {

    private var limiter: BotRateLimiter =
        BotRateLimiter(javabotConfig.chatGptLimit(), Duration.days(1).toMilliseconds())

    fun sendPromptToChatGPT(prompt: String): String? {
        return if (javabotConfig.chatGptKey().isNotEmpty() && limiter.tryAcquire()) {
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
