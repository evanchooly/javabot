package javabot.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.PersistenceException;

import javabot.dao.AbstractDaoImpl;
import javabot.dao.ChannelDao;
import javabot.dao.util.QueryParam;
import javabot.model.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelDaoImpl extends AbstractDaoImpl<Channel> implements ChannelDao {
    private static final Logger log = LoggerFactory.getLogger(ChannelDaoImpl.class);
    private final Map<String, Boolean> logCache = new HashMap<String, Boolean>();

    public ChannelDaoImpl() {
        super(Channel.class);
    }

    @Override
    public void delete(final Long id) {
        delete(find(id));
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<String> configuredChannels() {
        return (List<String>) getEntityManager().createNamedQuery(CONFIGURED_CHANNELS).getResultList();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Channel> getChannels() {
        return (List<Channel>) getEntityManager().createNamedQuery(ChannelDao.ALL).getResultList();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Channel> find(final QueryParam qp) {
        final StringBuilder query = new StringBuilder("from Channel c");
        if (qp.hasSort()) {
            query.append(" order by ")
                .append(qp.getSort())
                .append(qp.isSortAsc() ? " asc" : " desc");
        }
        return getEntityManager().createQuery(query.toString())
            .setFirstResult(qp.getFirst())
            .setMaxResults(qp.getCount()).getResultList();
    }

    @Override
    public boolean isLogged(final String channel) {
        Boolean logged = logCache.get(channel);
        if (logged == null) {
            final Channel chan = get(channel);
            if (chan != null) {
                logged = chan.getLogged();
                logCache.put(channel, logged);
            } else {
                logged = Boolean.FALSE;
            }
        }
        return logged;
    }

    @Override
    public Channel get(final String name) {
        Channel channel = null;
        if (name.startsWith("#")) {
            try {
                channel = (Channel) getEntityManager().createNamedQuery(ChannelDao.BY_NAME)
                    .setParameter("channel", name)
                    .getSingleResult();
                return channel;
            } catch (PersistenceException e) {
                log.debug(e.getMessage(), e);
            }
        }
        return channel;

    }

    @Override
    public Channel create(final String name, final Boolean logged, final String key) {
        final Channel channel = new Channel();
        channel.setName(name);
        channel.setLogged(logged == null ? Boolean.TRUE : logged);
        channel.setKey(key);
        save(channel);
        return channel;
    }

    @Override
    public void save(final Channel channel) {
        channel.setUpdated(new Date());
        merge(channel);
    }
}