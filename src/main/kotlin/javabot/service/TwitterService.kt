package javabot.service

import com.google.inject.Inject
import com.google.inject.Singleton
import javabot.JavabotConfig
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

@Singleton
class TwitterService @Inject constructor(private val config: JavabotConfig) {
    private val twitter: Twitter?

    init {
        twitter = if (config.twitterConsumerKey().isNotBlank()
                && config.twitterConsumerSecret().isNotBlank()
                && config.twitterAccessToken().isNotBlank()
                && config.twitterAccessTokenSecret().isNotBlank()) {
            val cb = ConfigurationBuilder()
            with(cb) {
                setOAuthConsumerKey(config.twitterConsumerKey())
                setOAuthConsumerSecret(config.twitterConsumerSecret())
                setOAuthAccessToken(config.twitterAccessToken())
                setOAuthAccessTokenSecret(config.twitterAccessTokenSecret())
            }
            val tf = TwitterFactory(cb.build())
            tf.instance
        } else {
            null
        }
    }

    fun isEnabled() = twitter != null

    fun getStatus(id: Long): String? {
        if (isEnabled()) {
            val status = twitter?.showStatus(id)
            if (status != null) {
                var text: String? = status.text ?: return null
                status.urlEntities.forEach { text = text?.replace(it.url, it.expandedURL) }
                return "${status.user.name} on Twitter: \"$text\""
            }
        }
        return null
    }
}