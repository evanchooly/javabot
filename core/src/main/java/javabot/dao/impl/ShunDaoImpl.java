package javabot.dao.impl;

import java.util.Date;

import javabot.dao.BaseDao;
import javabot.dao.ShunDao;
import javabot.model.Shun;
import org.springframework.stereotype.Component;

@Component
public class ShunDaoImpl extends BaseDao<Shun> implements ShunDao {

  public ShunDaoImpl() {
    super (Shun.class);
  }

  public boolean isShunned (final String nick) {
    return getShun (nick) != null;
  }

  public Shun getShun (final String nick) {
      expireShuns ();

      return null;//(Shun) getEntityManager ().createNamedQuery (ShunDao.BY_NAME)
//          .setParameter ("nick", nick).getSingleResult ();
  }

  private void expireShuns () {
//    getEntityManager ().createNamedQuery (ShunDao.CLEANUP)
//        .setParameter ("now", new Date ()).executeUpdate ();
  }

  public void addShun (final String nick, final Date until) {
    Shun shun = getShun (nick);
    if (shun == null) {
      shun = new Shun ();
      shun.setNick (nick);
      shun.setExpiry (until);

      save (shun);
    }
  }
}