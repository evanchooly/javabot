package javabot.dao.impl;

import java.util.Date;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.ShunDao;
import javabot.model.Shun;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShunDaoHibernate extends AbstractDaoHibernate<Shun> implements
    ShunDao {
    private static final Logger log = LoggerFactory.getLogger(ShunDaoHibernate.class);

  public ShunDaoHibernate () {
    super (Shun.class);
  }

  public boolean isShunned (String nick) {
    return getShun (nick) != null;
  }

  public Shun getShun (String nick) {
    try {
      expireShuns ();

      return (Shun) getEntityManager ().createNamedQuery (ShunDao.BY_NAME)
          .setParameter ("nick", nick).getSingleResult ();
    } catch (NoResultException e) {
      // hasn't been seen yet.
      return null;
    }
  }

  private void expireShuns () {
    getEntityManager ().createNamedQuery (ShunDao.CLEANUP)
        .setParameter ("now", new Date ()).executeUpdate ();
  }

  public void addShun (String nick, Date until) {
    Shun seen = getShun (nick);
    if (seen == null) {
      seen = new Shun ();
      seen.setNick (nick);
      seen.setExpiry (until);

      save (seen);
    }
  }

}