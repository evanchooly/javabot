package utils

import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken
import java.io.Serializable
import controllers.AdminController
import com.google.inject.Inject
import javax.inject.Named

class TwitterContext extends Serializable {

  @Inject
  @Named("twitter.key")
  private var twitterKey: String = null

  @Inject
  @Named("twitter.secret")
  private var twitterSecret: String = null

  def authenticate(oauth_token: String, oauth_verifier: String) {
    twitter.setOAuthConsumer(twitterKey, twitterSecret)
    val token: AccessToken = twitter.getOAuthAccessToken(new RequestToken(oauth_token, oauth_verifier))
    screenName = token.getScreenName
  }

  var screenName: String = null
  val twitter = new TwitterFactory().getInstance
}