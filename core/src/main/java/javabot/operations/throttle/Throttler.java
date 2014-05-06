package javabot.operations.throttle;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.inject.Inject;

import com.antwerkz.sofia.Sofia;
import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.core.ConditionTimeoutException;
import static java.time.LocalDateTime.now;
import javabot.Javabot;
import javabot.MyPircBot;
import javabot.dao.AdminDao;
import javabot.dao.BaseDao;
import javabot.dao.ConfigDao;
import javabot.dao.NickServDao;
import javabot.model.IrcUser;
import javabot.model.NickServInfo;
import javabot.model.ThrottleItem;
import javabot.model.criteria.ThrottleItemCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Throttler extends BaseDao<ThrottleItem> {
  private static final Logger LOG = LoggerFactory.getLogger(Throttler.class);

  @Inject
  private ConfigDao configDao;

  @Inject
  private AdminDao adminDao;

  @Inject
  private NickServDao nickServDao;

  @Inject
  private Javabot javabot;

  protected Throttler() {
    super(ThrottleItem.class);
  }

  /**
   * Check if a user is currently throttled or not.
   *
   * @return true if the user is currently throttled and ought to be ignored, false otherwise.
   */
  public boolean isThrottled(final IrcUser user, final MyPircBot myPircBot) {
    AtomicReference<NickServInfo> info = new AtomicReference<>(nickServDao.findByNick(user.getNick()));
    if(info.get() == null) {
      myPircBot.sendMessage("NickServ", "info " + user.getNick());
      LOG.info("Waiting for NickServ info on " + user.getNick());
      try {
        Awaitility.await()
            .atMost(3, TimeUnit.SECONDS)
            .until(() -> {
              info.set(nickServDao.findByNick(user.getNick()));
              return info.get() != null;
            });
      } catch (ConditionTimeoutException e) {
        LOG.info("Could not find NickServ registration info for " + user.getNick());
        throw new NickServViolationException(Sofia.unknownUser());
      }
    }
    NickServInfo nickServInfo = info.get();
    if(nickServInfo == null) {
      LOG.info("Could not find NickServ registration info for " + user.getNick());
      throw new NickServViolationException(Sofia.unknownUser());
    }
    if(Duration.between(nickServInfo.getRegistered(), now()).toDays() < configDao.get().getMininumNickServAge()) {
      throw new NickServViolationException(Sofia.accountTooNew());
    }

    if (!adminDao.isAdmin(user.getNick(), user.getHost())) {
      ds.save(new ThrottleItem(user));
      ThrottleItemCriteria criteria = new ThrottleItemCriteria(ds);
      criteria.user(user.getUserName());
      return criteria.query().countAll() > configDao.get().getThrottleThreshold();
    }
    return false;
  }

}
