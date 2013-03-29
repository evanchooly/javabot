package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

import com.google.inject.Injector;
import javabot.dao.AdminDao;
import javabot.dao.ChannelDao;
import javabot.dao.FactoidDao;
import javabot.dao.LogsDao;
import models.Admin;
import models.Change;
import models.Factoid;
import models.Karma;
import models.Log;
import play.data.binding.As;
import play.db.jpa.GenericModel;
import play.modules.paginate.ModelPaginator;
import play.modules.router.Get;
import play.modules.router.ServeStatic;
import play.modules.router.StaticRoutes;
import play.mvc.Controller;

@StaticRoutes({
  @ServeStatic(value = "/public/", directory = "public")
})
public class Application extends Controller {
  private static final int PAGE_SIZE = 50;

  @Inject
  private static Injector injector;

  @Get("/")
  public static void index() {
    Context context = new Context();
    render(context);
  }

  @Get("/logs/{channel}/{date}/?")
  public static void logs(String channel, @As("yyyy-MM-dd") Date date) {
    Context context = injector.getInstance(Context.class);
    if (date == null) {
      date = new Date();
    }
    context.logChannel(channel, date);
    Date before = add(date, -1);
    Date after = add(date, 1);
    render(context, channel, date, before, after);
  }

  private static Date add(Date start, int i) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(start);
    cal.add(Calendar.DATE, i);
    return cal.getTime();
  }

  @Get("/factoids/?")
  public static void factoids(String factoidName, String factoidValue, String userName) {
    Context context = new Context();
    StringBuilder builder = new StringBuilder();
    List<String> params = new ArrayList<String>();
    append(builder, params, factoidName, "name");
    append(builder, params, factoidValue, "value");
    append(builder, params, userName, "userName");
    if (params.isEmpty()) {
      context.paginator = new ModelPaginator<Factoid>(Factoid.class).orderBy("name ASC");
    } else {
      context.paginator = new ModelPaginator<Factoid>(Factoid.class,
        builder.toString(), params.toArray()).orderBy("name ASC");
    }
    context.paginator.setPageSize(PAGE_SIZE);
    if (factoidName == null) {
      factoidName = "";
    }
    if (factoidValue == null) {
      factoidValue = "";
    }
    if (userName == null) {
      userName = "";
    }
    render(context, factoidName, factoidValue, userName);
  }

  private static void append(StringBuilder builder, List<String> params, String value, final String attribute) {
    if (value != null) {
      if (builder.length() != 0) {
        builder.append(" and ");
      }
      builder.append(attribute + " like ?");
      params.add("%" + value + "%");
    }
  }

  @Get("/karma/?")
  public static void karma() {
    Context context = new Context();
    context.paginator = new ModelPaginator<Karma>(Karma.class).orderBy("value DESC");
    context.paginator.setPageSize(PAGE_SIZE);
    render(context);
  }

  @Get("/changes/?")
  public static void changes(String message) {
    Context context = new Context();
    if (message != null) {
      context.paginator = new ModelPaginator<Change>(Change.class, "message like ?", "%" + message + "%");
    } else {
      context.paginator = new ModelPaginator<Change>(Change.class);
    }
    context.paginator.orderBy("changeDate DESC");
    context.paginator.setPageSize(PAGE_SIZE);
    render(context, message);
  }

  @Get("/login/?")
  public static void login() {
    AdminController.oauth();
    index();
  }

  public static class Context<T extends GenericModel> {

    @Inject
    private FactoidDao factoidDao;
    @Inject
    private ChannelDao channelDao;
    @Inject
    private AdminDao adminDao;
    @Inject
    private LogsDao logsDao;

    final Long factoidCount;
    List<Log> logs;
    ModelPaginator<T> paginator;
    TwitterContext twitterContext;
    Date today = new Date();

    public Context() {
      factoidCount = Factoid.count();
    }

    public Boolean getShowAll() {
      Boolean showAll = Boolean.FALSE;
      twitterContext = AdminController.getTwitterContext();
      if(twitterContext != null) {
        if(twitterContext.screenName != null) {
          Admin admin = Admin.find("byUsername", twitterContext.screenName).first();
          showAll = admin != null && admin.botOwner;
        }
      }
      return showAll;
    }

    public Long getFactoidCount() {
      return factoidCount;
    }

    public List<javabot.model.Channel> getChannels() {
      return channelDao.findLogged(getShowAll());
    }

    public List<Log> getLogs() {
      return logs;
    }

    public void logChannel(String channel, Date date) {
      logs = logsDao.findByChannel(channel, date, getShowAll());
    }
  }
}