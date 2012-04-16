package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import models.JavabotRoleHolder;
import play.cache.Cache;
import play.modules.router.Get;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import play.utils.Properties;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

@With(Deadbolt.class)
public class Admin extends Controller {
    private static final String CONTEXT_NAME = "-context";
    private static final String twitterKey;
    private static final String twitterSecret;

    static {
        try {
            Properties props = new Properties();
            InputStream stream = Admin.class.getResourceAsStream("/oauth.conf");
            try {
                props.load(stream);
            } finally {
                stream.close();
            }

            twitterKey = props.get("twitter.key");
            twitterSecret = props.get("twitter.secret");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Before
    public static void oauth() {
        try {
            if (getTwitterContext() == null) {
                TwitterContext context = new TwitterContext();
                Cache.set(session.getId() + CONTEXT_NAME, context);
                RequestToken requestToken = context.twitter.getOAuthRequestToken(request.getBase() + "/callback");
                redirect(requestToken.getAuthenticationURL());
            }
        } catch (TwitterException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Get("/callback")
    public static void callback(String oauth_token, String oauth_verifier) {
        try {
            final TwitterContext context = getTwitterContext();
            context.authenticate(oauth_token, oauth_verifier);
            index();
        } catch (TwitterException e) {
            System.out.println("e = " + e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Get("/admin")
    @Restrict(JavabotRoleHolder.BOT_ADMIN)
    public static void index() {
        Application.Context context = new Application.Context();
        render(context);
    }

    public static TwitterContext getTwitterContext() {
        return (TwitterContext) Cache.get(session.getId() + CONTEXT_NAME);
    }

    public static class TwitterContext implements Serializable {
        String screenName;
        private Twitter twitter;

        public TwitterContext() {
            twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(twitterKey, twitterSecret);
        }

        public void authenticate(String oauth_token, String oauth_verifier) throws TwitterException {
            AccessToken token = twitter.getOAuthAccessToken(new RequestToken(oauth_token, oauth_verifier));
            screenName = token.getScreenName();
        }
    }
}