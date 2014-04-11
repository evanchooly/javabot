package controllers;

import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;

import javabot.dao.AdminDao;
import javabot.dao.ChangeDao;
import javabot.dao.KarmaDao;
import javabot.model.Factoid;
import models.Page;
import play.data.Form;
import play.libs.F.Tuple;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Result;
import security.OAuthDeadboltHandler;
import utils.Context;
import utils.FactoidDao;
import views.html.index;
import views.html.factoids;

public class Application extends Controller {
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

  public Result index() {
    return ok(index.render(handler, contextProvider.get()));
  }

  public Result showFactoids() throws UnsupportedEncodingException {
    Request request = Http.Context.current().request();
    String page = request.getQueryString("page");
    Form<Factoid> form = Form.form(Factoid.class).bindFromRequest();
    int pageNumber = page != null ? Integer.parseInt(page) : 0;
    Tuple<Long, List<Factoid>> pair = factoidDao.find(form.get(), pageNumber * PerPageCount, PerPageCount);
    Page<Factoid> content = new Page<>(form, routes.Application.showFactoids(), pageNumber, pageNumber * PerPageCount,
        pair._1, pair._2
    );
    return ok(factoids.apply(handler, contextProvider.get(), form, content));

  }

  public Result karma() {
    return null;
  }

  public Result changes() {
    return null;
  }

  public Result logs(String name, String date) {
    return null;
  }

}