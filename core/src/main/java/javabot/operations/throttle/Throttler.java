package javabot.operations.throttle;

import javax.inject.Inject;

import javabot.dao.AdminDao;
import javabot.dao.BaseDao;
import javabot.dao.ConfigDao;
import javabot.model.IrcUser;
import javabot.model.ThrottleItem;
import javabot.model.criteria.ThrottleItemCriteria;

public class Throttler extends BaseDao<ThrottleItem> {
  @Inject
  private ConfigDao configDao;
  @Inject
  private AdminDao adminDao;

  protected Throttler() {
    super(ThrottleItem.class);
  }

  /**
   * Check if a user is currently throttled or not.
   *
   * @return true if the user is currently throttled and ought to be ignored, false otherwise.
   */
  public boolean isThrottled(final IrcUser user) {
    if(!adminDao.isAdmin(user.getNick(), user.getHost())) {
      ds.save(new ThrottleItem(user));
      ThrottleItemCriteria criteria = new ThrottleItemCriteria(ds);
      criteria.user(user.getUserName());
      return criteria.query().countAll() > configDao.get().getThrottleThreshold();
    }
    return false;
  }

}
