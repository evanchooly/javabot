package javabot.model;

import com.antwerkz.sofia.Sofia;
import javabot.Javabot;
import javabot.dao.ChannelDao;
import javabot.dao.EventDao;

import javax.persistence.Entity;

@Entity
public class ChannelEvent extends AdminEvent {
    private String channel;
    private String key;
    private Boolean logged;

    protected ChannelEvent() {
    }

    public ChannelEvent(String channel, EventType type, String requestedBy) {
       this(channel, null, type, requestedBy);
    }
    public ChannelEvent(String channel, String key, EventType type, String requestedBy) {
        super(type, requestedBy);
        this.key = key;
        this.channel = channel;
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
        Channel chan = bot.getApplicationContext().getBean(ChannelDao.class).get(channel);
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
