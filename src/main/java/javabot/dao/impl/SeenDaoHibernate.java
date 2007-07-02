package javabot.dao.impl;

import java.util.Date;
import javax.persistence.NoResultException;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.SeenDao;
import javabot.model.Seen;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:22 PM

public class SeenDaoHibernate extends AbstractDaoHibernate<Seen> implements SeenDao {
    private static final Log log = LogFactory.getLog(SeenDaoHibernate.class);

    public SeenDaoHibernate() {
        super(Seen.class);
    }

    public void logSeen(String nick, String channel, String message) {
        Seen seen = getSeen(nick, channel);
        if(seen == null) {
            seen = new Seen();
            seen.setNick(nick);
            seen.setChannel(channel);
        }
        seen.setMessage(message);
        seen.setUpdated(new Date());
        save(seen);
    }

    public boolean isSeen(String nick, String channel) {
        return getSeen(nick, channel).getNick() != null;
    }

    public Seen getSeen(String nick, String channel) {
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