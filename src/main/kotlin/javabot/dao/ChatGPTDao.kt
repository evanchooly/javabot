package javabot.dao

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.cache.CacheBuilder
import com.google.inject.Inject
import com.google.inject.Singleton
import io.dropwizard.util.Duration
import javabot.JavabotConfig
import javabot.operations.throttle.BotRateLimiter
import javabot.service.HttpService
import org.apache.commons.compress.harmony.unpack200.NewAttributeBands.Callable
import java.util.concurrent.TimeUnit

data class GPTMessageContainer(
    val messages: List<GPTMessage>,
    val model: String = "gpt-4",
    val temperature: Double = 0.7
)

data class GPTMessage(val content: String, val role: String = "user")

class GPTResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("object") val completion: String,
    @JsonProperty("created") val created: Long,
    @JsonProperty("model") val model: String,
    @JsonProperty("choices") val choices: List<GPTChoice>,
    @JsonProperty("usage") val usage: GPTUsage
)

class GPTUsage(
    @JsonProperty("prompt_tokens") val promptTokens: Int,
    @JsonProperty("completion_tokens") val completionTokens: Int,
    @JsonProperty("total_tokens") val totalTokens: Int
)

class GPTChoice(
    @JsonProperty("message") val message: GPTChoiceMessage,
    @JsonProperty("index") val index: Int,
    @JsonProperty("finish_reason") val finishReason: String
)

data class GPTChoiceMessage(
    @JsonProperty("content") val content: String,
    @JsonProperty("role") val role: String
)

@Singleton
class ChatGPTDao
@Inject
constructor(
    private val javabotConfig: JavabotConfig,
    private val httpService: HttpService,
) {
    private val mapper: ObjectMapper

    private val limiter: BotRateLimiter =
        BotRateLimiter(javabotConfig.chatGptLimit(), Duration.days(1).toMilliseconds())
    private val queryCache = CacheBuilder.newBuilder()
        .maximumSize(100)
        .expireAfterWrite(1, TimeUnit.DAYS)
        .build<String, GPTResponse>();

    init {
        mapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    private fun getGPTResponse(key: String, prompt: String): GPTResponse {
        val data = httpService.post(
            "https://api.openai.com/v1/chat/completions",
            emptyMap(),
            mapOf("Authorization" to "Bearer ${javabotConfig.chatGptKey()}"),
            emptyMap(),
            GPTMessageContainer(listOf(GPTMessage(prompt)))
        )
        val response = mapper.readValue(data, GPTResponse::class.java)
        return response
    }

    fun sendPromptToChatGPT(key: String, prompt: String): String? {
        val mapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return if (javabotConfig.chatGptKey().isNotEmpty() && limiter.tryAcquire()) {
            val response = queryCache.get(key) { getGPTResponse(key, prompt) }
            return response.choices.first().message.content
        } else {
            // no chatGPT key? No chatGPT attempt.
            null
        }
    }
}
