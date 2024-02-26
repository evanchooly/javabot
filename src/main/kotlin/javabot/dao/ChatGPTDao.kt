package javabot.dao

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.google.inject.Singleton
import io.dropwizard.util.Duration
import javabot.JavabotConfig
import javabot.operations.throttle.BotRateLimiter
import javabot.service.HttpService

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
    private var limiter: BotRateLimiter =
        BotRateLimiter(javabotConfig.chatGptLimit(), Duration.days(1).toMilliseconds())

    fun sendPromptToChatGPT(prompt: String): String? {
        val mapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return if (javabotConfig.chatGptKey().isNotEmpty() && limiter.tryAcquire()) {
            val data =
                httpService.post(
                    "https://api.openai.com/v1/chat/completions",
                    emptyMap(),
                    mapOf("Authorization" to "Bearer ${javabotConfig.chatGptKey()}"),
                    emptyMap(),
                    GPTMessageContainer(listOf(GPTMessage(prompt)))
                )
            val response = mapper.readValue(data, GPTResponse::class.java)
            return response.choices.first().message.content
        } else {
            // no chatGPT key? No chatGPT attempt.
            null
        }
    }
}
