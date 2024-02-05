package javabot.dao

import com.google.inject.Inject
import gpt.api.GPT
import gpt.models.Message
import gpt.models.MessageModel
import io.dropwizard.util.Duration
import javabot.JavabotConfig
import javabot.operations.throttle.BotRateLimiter


class ChatGPTDao
@Inject
constructor(val javabotConfig: JavabotConfig) {
    companion object {
        private var limiter: BotRateLimiter? = null

        fun acquire(javabotConfig: JavabotConfig): Boolean {
            if (limiter == null) {
                limiter = BotRateLimiter(
                    javabotConfig.chatGptLimit(),
                    Duration.days(1).toMilliseconds()
                )
            }
            return limiter!!.tryAcquire()
        }
    }

    fun sendPromptToChatGPT(prompt: String): String? {
        return if (javabotConfig.chatGptKey().isNotEmpty() && acquire(javabotConfig)) {
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
