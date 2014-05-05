package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.inject.Inject;
import javax.inject.Provider;

import javabot.dao.AdminDao;
import javabot.model.Change;
import javabot.model.Factoid;
import javabot.model.Karma;
import models.Page;
import java.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Result;
import security.OAuthDeadboltHandler;
import utils.ChangeDao;
import utils.Context;
import utils.FactoidDao;
import utils.KarmaDao;
import views.html.factoids;
import views.html.index;

public class Application extends Controller {
  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

  @Inject
  private AdminDao adminDao;

  @Inject
  private OAuthDeadboltHandler handler;

  @Inject
  private Provider<Context> contextProvider;

  @Inject
  private FactoidDao factoidDao;

  @Inject
  private ChangeDao changeDao;

  @Inject
  private KarmaDao karmaDao;

  private static final int PerPageCount = 50;

  private static final String PATTERN = "yyyy-MM-dd";

  public Result index() {
    return ok(index.render(handler, contextProvider.get()));
  }

  public Result showFactoids() {
    Request request = Http.Context.current().request();
    String page = request.getQueryString("page");
    int pageNumber = page != null ? Integer.parseInt(page) : 0;
    Form<Factoid> form = Form.form(Factoid.class).bindFromRequest();
    Page<Factoid> content = new Page<>(form, routes.Application.showFactoids(), pageNumber, pageNumber * PerPageCount,
        factoidDao.find(form.get(), pageNumber * PerPageCount, PerPageCount));
    return ok(factoids.apply(handler, contextProvider.get(), form, content));

  }

  public Result karma() {
    Request request = Http.Context.current().request();
    String page = request.getQueryString("page");
    Form<Karma> form = Form.form(Karma.class).bindFromRequest();
    int pageNumber = page != null ? Integer.parseInt(page) : 0;
    Page<Karma> pageContent = new Page<>(form, routes.Application.karma(), pageNumber, pageNumber * PerPageCount,
        karmaDao.find(pageNumber * PerPageCount, PerPageCount));
    return ok(views.html.karma.apply(handler, contextProvider.get(), form, pageContent));
  }

  public Result changes() {
    Request request = Http.Context.current().request();
    String page = request.getQueryString("page");
    int pageNumber = page != null ? Integer.parseInt(page) : 0;
    Form<Change> form = Form.form(Change.class).bindFromRequest();
    Page<Change> content = new Page<>(form, routes.Application.changes(), pageNumber, pageNumber * PerPageCount,
        changeDao.find(form.get(), pageNumber * PerPageCount, PerPageCount));
    return ok(views.html.changes.apply(handler, contextProvider.get(), form, content));
  }

  public Result logs(String channel, String dateString) {
    DateTime date;
    DateTimeFormatter format = DateTimeFormat.forPattern(PATTERN);
    String channelName;
    try {
      channelName = URLDecoder.decode(channel, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      LOG.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
    try {
      if ("today".equals(dateString)) {
        date = DateTime.now().withTimeAtStartOfDay();
      } else {
        date = DateTime.parse(dateString, format);
      }
    } catch (Exception e) {
      date = DateTime.now().withTimeAtStartOfDay();
    }

    Context context = contextProvider.get();
    context.logChannel(channelName, date);

    return ok(views.html.logs.apply(handler, context, channelName,
        date.toString(PATTERN),
        date.minusDays(1).toString(PATTERN),
        date.plusDays(1).toString(PATTERN)));
  }
}