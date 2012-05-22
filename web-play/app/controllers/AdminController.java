package controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import models.Admin;
import models.Api;
import models.ApiEvent;
import models.Channel;
import models.Config;
import models.Factoid;
import models.JavabotRoleHolder;
import models.NickRegistration;
import play.cache.Cache;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.libs.Files;
import play.libs.IO;
import play.modules.router.Get;
import play.modules.router.Post;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import play.utils.Properties;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

@With(Deadbolt.class)
public class AdminController extends Controller {
  private static final String CONTEXT_NAME = "-context";
  static final String twitterKey;
  static final String twitterSecret;
  public static final List<String> OPERATIONS;

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
      stream = AdminController.class.getResourceAsStream("/operations.list");
      try {
        OPERATIONS = IO.readLines(stream);
      } finally {
        stream.close();
      }

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
      if (Admin.findAll().isEmpty()) {
        Admin admin = new Admin("", "", getTwitterContext().screenName, "auto added bot owner");
        admin.botOwner = true;
        admin.save();
        index();
      }
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

  @Get("/config")
  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  public static void config() {
    Application.Context context = new Application.Context();
    Config config = (Config) Config.findAll().get(0);
    List<String> operations = OPERATIONS;
    render(context, config, operations);
  }

  @Get("/saveConfig")
  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  public static void saveConfig(Config config) {
    List<Config> all = Config.findAll();
    Config old = (Config) all.get(0);
    if (!old.operations.equals(config.operations)) {
      Config.updateOperations(old.operations, config.operations);
    }
    config.id = old.id;
    Config updated = config.merge();
    while (updated.url.endsWith("/")) {
      updated.url = updated.url.substring(0, updated.url.length() - 1);
    }
    updated.save();
    index();
  }

  @Get("/javadoc")
  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  public static void javadoc() {
    Application.Context context = new Application.Context();
    List<Api> apis = Api.find("order by name").fetch();
    render(context, apis);
  }

  @Post("/addJavadoc")
  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  public static void addJavadoc(String name, String packages, String baseUrl, File file) throws IOException {
    File savedFile = File.createTempFile("javadoc", ".jar");
    Files.copy(file, savedFile);
    new ApiEvent(Api.find("byName", name).<Api>first() == null, AdminController.getTwitterContext().screenName,
      name, packages, baseUrl, savedFile).save();
    javadoc();
  }

  @Post("/deleteApi")
  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  public static void deleteApi(Long id) throws IOException {
    ApiEvent event = new ApiEvent(id, AdminController.getTwitterContext().screenName);
    event.save();
    javadoc();
  }

  @Post("/addAdmin")
  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  public static void addAdmin(String ircName, String hostName, String twitter) {
    if (!twitter.isEmpty()) {
      new Admin(ircName, hostName, twitter, getTwitterContext().screenName).save();
    }
    index();
  }

  @Get("/deleteAdmin")
  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  public static void deleteAdmin(Long id) {
    Admin admin = Admin.findById(id);
    if (admin != null && !admin.botOwner) {
      admin.delete();
    }
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
    if (Validation.hasErrors()) {
      params.flash(); // add http parameters to the flash scope
      Validation.keep(); // keep the errors for the next request
      editChannel(channel);
    }
    channel.updated = new Date();
    channel.save();
    index();
  }

  @Get("/toggleLock")
  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  public static void toggleLock(Long id) {
    Factoid factoid = Factoid.findById(id);
    factoid.locked = !factoid.locked;
    factoid.save();
    renderJSON(factoid.locked);
  }

  @Get("/register")
  @Restrict(JavabotRoleHolder.BOT_ADMIN)
  public static void registerAdmin(String id) {
    NickRegistration registration = NickRegistration.find("byUrl", id).first();
    if (registration != null && getTwitterContext().screenName.equals(registration.twitterName)) {
      Admin admin = Admin.find("byUsername", registration.twitterName).first();
      if (admin != null) {
        admin.ircName = registration.nick;
        admin.hostName = registration.host;
        admin.save();
      }
      registration.delete();
    }
    index();
  }

  public static TwitterContext getTwitterContext() {
    return (TwitterContext) Cache.get(session.getId() + CONTEXT_NAME);
  }

}