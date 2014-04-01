package javabot.dao;

import javabot.model.Shun;
import javabot.model.criteria.ShunCriteria;
import org.joda.time.DateTime;

public class ShunDao extends BaseDao<Shun> {

  public ShunDao() {
    super(Shun.class);
  }

  public boolean isShunned(final String nick) {
    return getShun(nick) != null;
  }

  public Shun getShun(final String nick) {
    expireShuns();
    ShunCriteria criteria = new ShunCriteria(ds);
    criteria.upperNick().equal(nick.toUpperCase());
    return criteria.query().get();
  }

  private void expireShuns() {
    ShunCriteria criteria = new ShunCriteria(ds);
    criteria.expiry().lessThan(new DateTime());
    ds.delete(criteria.query());
  }

  public void addShun(final String nick, final DateTime until) {
    Shun shun = getShun(nick);
    if (shun == null) {
      shun = new Shun();
      shun.setNick(nick.toUpperCase());
      shun.setExpiry(until);
      save(shun);
    }
  }
}
