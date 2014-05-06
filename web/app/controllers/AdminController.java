package controllers;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.Unrestricted;
import javabot.dao.ApiDao;
import javabot.dao.ChannelDao;
import javabot.javadoc.JavadocApi;
import javabot.model.ApiEvent;
import javabot.model.Channel;
import javabot.model.Config;
import javabot.model.EventType;
import models.Admin;
import org.bson.types.ObjectId;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.java.JavaController;
import org.pac4j.play.java.RequiresAuthentication;
import play.data.Form;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Session;
import play.mvc.Result;
import security.OAuthDeadboltHandler;
import utils.AdminDao;
import utils.ConfigDao;
import utils.Context;
import views.html.admin.config;

@Singleton
@Restrict(value = {@Group("botAdmin")}/*, handlerKey = "security.OAuthDeadboltHandler"*/)
@RequiresAuthentication(clientName = "Google2Client")
public class AdminController extends JavaController {
  @Inject
  private ConfigDao configDao;

  @Inject
  private AdminDao adminDao;

  @Inject
  private ApiDao apiDao;

  @Inject
  private ChannelDao channelDao;

  @Inject
  private Provider<Context> contextProvider;

  @Inject
  private OAuthDeadboltHandler handler;

  private javabot.model.Admin getAdmin() {
    return adminDao.getAdmin(Http.Context.current().session().get("userName"));
  }

  @Unrestricted
  public Result login() {
    CommonProfile profile = getUserProfile();
    if (adminDao.findAll().isEmpty()) {
      LOG.info("creating new admin");
      adminDao.create("", profile.getEmail(), "");
    }
    Session session = Http.Context.current().session();
    session.put("userName", profile.getEmail());
    return found(routes.AdminController.index());
  }

  public Result index() {
    Request request = Http.Context.current().request();
    return ok(views.html.admin.admin.apply(handler, contextProvider.get(),
        Form.form(Admin.class).bindFromRequest(request), adminDao.findAll()));

  }

  public Result showConfig() {
    return ok(views.html.admin.config.apply(handler, contextProvider.get(),
        Form.form(Config.class), configDao.get().getOperations(), configDao.operations()));

  }

  public Result javadoc() {
    return ok(views.html.admin.javadoc.apply(handler, contextProvider.get(), apiDao.findAll()));
  }

  public Result showChannel(String name) {
    Channel channel = channelDao.get(name);
    Form<Channel> form = Form.form(Channel.class);
    if(channel == null) {
      channel = new Channel();
      channel.setName(name);
    }
    form = form.fill(channel);
    return ok(views.html.admin.editChannel.apply(handler, contextProvider.get(), form));
  }

  public Result addAdmin() {
    Form<javabot.model.Admin> form = Form.form(javabot.model.Admin.class).bindFromRequest();
    adminDao.save(form.get());
    return found(routes.AdminController.index());
  }

  public Result deleteAdmin(String id) {
    javabot.model.Admin admin = adminDao.find(new ObjectId(id));
    if (admin != null && !admin.getBotOwner()) {
      adminDao.delete(admin);
    }
    return found(routes.AdminController.index());
  }

  public Result saveConfig() {
    javabot.model.Admin admin = adminDao.getAdmin(Http.Context.current().session().get("userName"));
    Form<Config> configForm = Form.form(Config.class).bindFromRequest();
    if (configForm.hasErrors()) {
      return badRequest(config.apply(new OAuthDeadboltHandler(), contextProvider.get(),
          configForm, configDao.get().getOperations(), configDao.operations()));
    } else {
      configDao.save(admin, configForm.get());
      return ok(routes.Application.index().method());
    }
  }

  public Result enableOperation(String name) {
    adminDao.enableOperation(name, adminDao.getAdmin(Http.Context.current().session().get("userName")).getUserName());
    return found(routes.AdminController.showConfig());
  }

  public Result disableOperation(String name) {
    adminDao.disableOperation(name, getAdmin().getUserName());
    return found(routes.AdminController.showConfig());
  }

  public Result reloadApi(String id) {
    apiDao.save(new ApiEvent(EventType.RELOAD, getAdmin().getUserName(), new ObjectId(id)));
    return found(routes.AdminController.javadoc());
  }

  public Result addApi() {
    JavadocApi api = Form.form(JavadocApi.class).bindFromRequest().get();
    apiDao.save(new ApiEvent(getAdmin().getUserName(), api.getName(), api.getBaseUrl(), api.getDownloadUrl()));
    return found(routes.AdminController.javadoc());
  }

  public Result deleteApi(String id) {
    apiDao.save(new ApiEvent(EventType.DELETE, getAdmin().getUserName(), new ObjectId(id)));
    return found(routes.AdminController.javadoc());
  }

  public Result saveChannel() {
    Channel channel = Form.form(Channel.class).bindFromRequest().get();
    channelDao.save(channel);
    return ok(config.apply(new OAuthDeadboltHandler(), contextProvider.get(), buildConfigForm(),
        configDao.get().getOperations(), configDao.operations()));
  }

  private Form<Config> buildConfigForm() {
    return Form.form(Config.class).fill(configDao.get());
  }
}