package security;

import javax.inject.Inject;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Http.Context;
import utils.AdminDao;

public class OAuthDeadboltHandler extends AbstractDeadboltHandler {
  @Inject
  private AdminDao adminDao;

  @Override
  public Promise<play.mvc.SimpleResult> beforeAuthCheck(final Context context) {
    return F.Promise.pure(null);
  }

  @Override
  public Subject getSubject(final Context context) {
    String userName = context.session().get("userName");
    return userName != null ? adminDao.getSubject(userName) : null;
  }
}
