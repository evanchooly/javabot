package javabot.dao

import com.enigmastation.kgpt.GPT
import com.enigmastation.kgpt.model.BaseGPTResponse
import com.enigmastation.kgpt.model.GPTMessage
import com.google.common.cache.CacheBuilder
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.time.Duration
import java.time.temporal.ChronoUnit.DAYS
import java.util.concurrent.TimeUnit
import javabot.JavabotConfig
import javabot.operations.throttle.BotRateLimiter

@Singleton
class ChatGPTDao
@Inject
constructor(
    private val javabotConfig: JavabotConfig,
) {
    private val gpt = GPT(javabotConfig.chatGptKey())

    private val limiter: BotRateLimiter =
        BotRateLimiter(javabotConfig.chatGptLimit(), Duration.of(1L, DAYS).toMillis())
    private val queryCache =
        CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build<String, BaseGPTResponse>()

    private fun getGPTResponse(prompts: List<GPTMessage>): BaseGPTResponse = gpt.query(prompts)

    fun sendPromptToChatGPT(key: String, prompts: List<GPTMessage>): String? {
        return if (limiter.tryAcquire()) {
            val response = queryCache.get(key) { getGPTResponse(prompts) }
            return response.first()
        } else {
            // no chatGPT key? No chatGPT attempt.
            null
        }
    }
}
