package javabot.dao

import com.enigmastation.kgpt.GPT
import com.enigmastation.kgpt.model.BaseGPTResponse
import com.enigmastation.kgpt.model.GPTMessage
import com.google.common.cache.CacheBuilder
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.time.Duration
import javabot.JavabotConfig
import javabot.operations.throttle.BotRateLimiter
import kotlin.time.Duration.Companion.days
import kotlin.time.toJavaDuration

@Singleton
class ChatGPTDao @Inject constructor(javabotConfig: JavabotConfig) {
    private val gpt = GPT(javabotConfig.chatGptKey())

    private val limiter: BotRateLimiter =
        BotRateLimiter(javabotConfig.chatGptLimit(), Duration.ofDays(1).toMillis())
    private val queryCache =
        CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(1.days.toJavaDuration())
            .build<String, BaseGPTResponse>()

    private fun getGPTResponse(prompts: List<GPTMessage>): BaseGPTResponse = gpt.query(prompts)

    fun sendPromptToChatGPT(key: String, prompts: List<GPTMessage>): String? {
        return if (limiter.tryAcquire()) {
            queryCache.get(key) { getGPTResponse(prompts) }.first()
        } else {
            // no chatGPT key? No chatGPT attempt.
            null
        }
    }
}
