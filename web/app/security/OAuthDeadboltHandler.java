package security;

import javax.inject.Inject;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.Constants;
import org.pac4j.play.StorageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Http.Session;
import utils.AdminDao;

public class OAuthDeadboltHandler extends AbstractDeadboltHandler {
  private static final Logger LOG = LoggerFactory.getLogger(OAuthDeadboltHandler.class);

  @Inject
  private static AdminDao adminDao;

  @Override
  public Promise<play.mvc.SimpleResult> beforeAuthCheck(final Context context) {
    return F.Promise.pure(null);
  }

  protected static CommonProfile getUserProfile() {
    // get the session id
    final String sessionId = Http.Context.current().session().get(Constants.SESSION_ID);
    LOG.debug("sessionId for profile : {}", sessionId);
    if (StringUtils.isNotBlank(sessionId)) {
      // get the user profile
      final CommonProfile profile = StorageHelper.getProfile(sessionId);
      LOG.debug("profile : {}", profile);
      return profile;
    }
    return null;
  }

  @Override
  public Subject getSubject(final Context context) {
    String userName = context.session().get("userName");
    return userName != null ? adminDao.getSubject(userName) : null;
  }
}
