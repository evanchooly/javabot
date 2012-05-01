package javabot.model;

import com.antwerkz.sofia.Sofia;
import javabot.Javabot;
import javabot.dao.ChannelDao;
import javabot.dao.EventDao;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "events")
@NamedQueries({
    @NamedQuery(name = EventDao.FIND_ALL, query = "select a from javabot.model.AdminEvent a where" +
        " a.processed = false order by a.requestedOn ASC")
})
public class ChannelEvent extends AdminEvent {
    private String channel;
    private String key;
    private Boolean logged;
    @Enumerated(EnumType.STRING)
    private ChannelEventType type;

    protected ChannelEvent() {
    }

    public ChannelEvent(String channel, ChannelEventType type, String requestedBy) {
       this(channel, null, type, requestedBy);
    }
    public ChannelEvent(String channel, String key, ChannelEventType type, String requestedBy) {
        super(requestedBy);
        this.key = key;
        this.type = type;
        this.channel = channel;
    }

    @Enumerated(EnumType.STRING)
    public ChannelEventType getType() {
        return type;
    }

    public void setType(ChannelEventType type) {
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getLogged() {
        return logged;
    }

    public void setLogged(Boolean logged) {
        this.logged = logged;
    }

    @Override
    public void handle(Javabot bot) {
        switch (type) {
            case ADD:
                add(bot);
                break;
            case DELETE:
                delete(bot);
                break;
            case UPDATE:
                update(bot);
                break;
        }

        setProcessed(true);
        bot.getApplicationContext().getBean(EventDao.class).save(this);
    }

    public void add(Javabot bot) {
        Channel chan = new Channel();
        chan.setName(channel);
        chan.setKey(key);
        chan.setLogged(logged);

        ChannelDao dao = bot.getApplicationContext().getBean(ChannelDao.class);
        dao.save(chan);
        chan.join(bot);
    }

    private void delete(Javabot bot) {
        ChannelDao dao = bot.getApplicationContext().getBean(ChannelDao.class);
        Channel chan = dao.get(channel);
        if(chan != null) {
            dao.delete(chan.getId());
            chan.leave(bot, Sofia.channelDeleted(getRequestedBy()));
        }
    }

    private void update(Javabot bot) {
        ChannelDao dao = bot.getApplicationContext().getBean(ChannelDao.class);
        Channel chan = dao.get(channel);
        if(chan != null) {
            chan.setLogged(logged);
            chan.setKey(key);
            dao.save(chan);
            chan.leave(bot, Sofia.channelUpdated());
            chan.join(bot);
        }
    }
}
