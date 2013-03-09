package javabot.dao;

import java.util.Date;

import javabot.model.Shun;
import javabot.model.criteria.ShunCriteria;

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
    criteria.expiry().lessThan(new Date());
    ds.delete(criteria.query());
  }

  public void addShun(final String nick, final Date until) {
    Shun shun = getShun(nick);
    if (shun == null) {
      shun = new Shun();
      shun.setNick(nick);
      shun.setExpiry(until);
      save(shun);
    }
  }
}
