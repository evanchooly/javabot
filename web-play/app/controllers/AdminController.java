package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import models.Admin;
import models.Channel;
import models.JavabotRoleHolder;
import play.cache.Cache;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.modules.router.Get;
import play.modules.router.Post;
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
import java.util.Date;
import java.util.List;

@With(Deadbolt.class)
public class AdminController extends Controller {
    private static final String CONTEXT_NAME = "-context";
    private static final String twitterKey;
    private static final String twitterSecret;

    static {
        try {
            Properties props = new Properties();
            InputStream stream = AdminController.class.getResourceAsStream("/oauth.conf");
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

    @Before(unless = "callback")
    public static void oauth() {
        try {
            TwitterContext twitterContext = getTwitterContext();
            if (twitterContext == null || twitterContext.screenName == null) {
                TwitterContext context = new TwitterContext();
                Cache.set(session.getId() + CONTEXT_NAME, context);
                RequestToken requestToken = context.twitter.getOAuthRequestToken(request.getBase() + "/callback");
                redirect(requestToken.getAuthenticationURL());
            }
        } catch (TwitterException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Get("/login")
    public static void login() {
        Application.index();
    }

    @Get("/callback")
    public static void callback(String oauth_token, String oauth_verifier) {
        try {
            getTwitterContext().authenticate(oauth_token, oauth_verifier);
            Application.index();
        } catch (TwitterException e) {
            System.out.println("e = " + e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Get("/admin")
    @Restrict(JavabotRoleHolder.BOT_ADMIN)
    public static void index() {
        Application.Context context = new Application.Context();
        List<Admin> admins = Admin.find("order by userName").fetch();
        render(context, admins);
    }

    @Post("/add")
    @Restrict(JavabotRoleHolder.BOT_ADMIN)
    public static void add(String name) {
        Admin admin = new Admin();
        admin.userName = name;
        admin.addedBy = getTwitterContext().screenName;
        admin.updated = new Date();
        admin.save();
        index();
    }

    @Post("/updateAdmin")
    @Restrict(JavabotRoleHolder.BOT_ADMIN)
    public static void updateAdmin(String userName, String ircName) {
        List<Admin> list = Admin.find("userName = ?", userName).fetch();
        Admin admin = list.get(0);
        admin.ircName = ircName;
        admin.save();

        index();
    }

    @Get("/addChannel")
    @Restrict(JavabotRoleHolder.BOT_ADMIN)
    public static void addChannel() {
        Application.Context context = new Application.Context();
        Channel channel = new Channel();
        renderTemplate("AdminController/editChannel.html", context, channel);
    }

    @Get("/showChannel")
    @Restrict(JavabotRoleHolder.BOT_ADMIN)
    public static void showChannel(String name) {
        Application.Context context = new Application.Context();
        Channel channel = Channel.find("name = ?1", name).<Channel>first();
        renderTemplate("AdminController/editChannel.html", context, channel);
    }

    @Get("/editChannel")
    public static void editChannel(Channel channel) {
        Application.Context context = new Application.Context();
        render(context, channel);
    }

    @Get("/saveChannel")
    @Restrict(JavabotRoleHolder.BOT_ADMIN)
    public static void saveChannel(@Valid Channel channel) {
        if(Validation.hasErrors()) {
            params.flash(); // add http parameters to the flash scope
            Validation.keep(); // keep the errors for the next request
            editChannel(channel);
        }
        channel.updated = new Date();
        channel.save();
        index();
    }

    public static TwitterContext getTwitterContext() {
        return (TwitterContext) Cache.get(session.getId() + CONTEXT_NAME);
    }

    public static class TwitterContext implements Serializable {
        public String screenName;
        private final Twitter twitter;

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