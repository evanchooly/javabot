package javabot.dao.impl;

import javax.persistence.NoResultException;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.SeenDao;
import javabot.model.Seen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM

public class SeenDaoImpl extends AbstractDaoImpl<Seen> implements SeenDao {
    private static final Logger log = LoggerFactory.getLogger(SeenDaoImpl.class);

    public SeenDaoImpl() {
        super(Seen.class);
    }

    @Override
    public boolean isSeen(final String nick, final String channel) {
        return getSeen(nick, channel) != null;
    }

    @Override
    public Seen getSeen(final String nick, final String channel) {
        Seen seen = null;
        try {
            seen = (Seen)getEntityManager().createNamedQuery(SeenDao.BY_NAME_AND_CHANNEL)
            .setParameter("nick", nick)
                .setParameter("channel", channel)
                .getSingleResult();
        } catch(NoResultException e) {
            // hasn't been seen yet.
        }
        return seen;
    }
}