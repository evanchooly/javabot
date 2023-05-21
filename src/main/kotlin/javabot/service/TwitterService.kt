package javabot.service

import com.google.inject.Inject
import com.google.inject.Singleton
import javabot.JavabotConfig
import twitter4j.Twitter

@Singleton
class TwitterService @Inject constructor(private val config: JavabotConfig) {
    private val twitter: Twitter?

    init {
        twitter =
            if (
                config.twitterConsumerKey().isNotBlank() &&
                    config.twitterConsumerSecret().isNotBlank() &&
                    config.twitterAccessToken().isNotBlank() &&
                    config.twitterAccessTokenSecret().isNotBlank()
            ) {
                Twitter.newBuilder()
                    .apply {
                        oAuthConsumer(config.twitterConsumerKey(), config.twitterConsumerSecret())
                        oAuthAccessToken(
                            config.twitterAccessToken(),
                            config.twitterAccessTokenSecret()
                        )
                    }
                    .build()
            } else {
                null
            }
    }

    fun isEnabled() = twitter != null

    fun getStatus(id: Long): String? {
        if (isEnabled()) {
            val status = twitter?.v1()?.tweets()?.showStatus(id)
            if (status != null) {
                var text: String = status.text ?: return null
                status.urlEntities.forEach { text = text.replace(it.url, it.expandedURL) }
                return "${status.user.name} on Twitter: \"$text\""
            }
        }
        return null
    }
}
