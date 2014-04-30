package javabot.operations.throttle;

import javax.inject.Inject;

import javabot.dao.BaseDao;
import javabot.dao.ConfigDao;
import javabot.model.IrcUser;
import javabot.model.ThrottleItem;
import javabot.model.criteria.ThrottleItemCriteria;

public class Throttler extends BaseDao<ThrottleItem> {
  @Inject
  private ConfigDao configDao;

  protected Throttler() {
    super(ThrottleItem.class);
  }

  /**
   * Check if an item is currently throttled or not.
   *
   * @return true if t is currently throttled and ought to be ignored, false otherwise.
   */
  public boolean isThrottled(final IrcUser user) {
    addThrottleItem(user);
    ThrottleItemCriteria criteria = new ThrottleItemCriteria(ds);
    criteria.user(user.getUserName());
    return criteria.query().countAll() > configDao.get().getThrottleThreshold();
  }

  /**
   * Add a new item to the throttle list.
   */
  public void addThrottleItem(final IrcUser user) {
    ds.save(new ThrottleItem(user));
  }
}
