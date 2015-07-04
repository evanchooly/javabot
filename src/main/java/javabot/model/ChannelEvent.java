package javabot.model;

import com.antwerkz.sofia.Sofia;
import javabot.dao.ChannelDao;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Transient;
import org.pircbotx.PircBotX;

import javax.inject.Inject;
import javax.inject.Provider;

@Entity("events")
public class ChannelEvent extends AdminEvent {
    @Inject
    @Transient
    private ChannelDao channelDao;
    @Inject
    @Transient
    private Provider<PircBotX> ircBot;
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
    public void add() {
        join(channelDao.create(channel, logged, key));
    }

    protected void join(final Channel chan) {
        if (chan.getKey() == null) {
            ircBot.get().sendIRC().joinChannel(chan.getName());
        } else {
            ircBot.get().sendIRC().joinChannel(chan.getName(), chan.getKey());
        }
    }

    @Override
    public void delete() {
        Channel chan = channelDao.get(channel);
        if (chan != null) {
            channelDao.delete(chan.getId());
            ircBot.get().getUserChannelDao().getChannel(channel).send().part(Sofia.channelDeleted(getRequestedBy()));
        }
    }

    @Override
    public void update() {
        Channel chan = channelDao.get(channel);
        if (chan != null) {
            chan.setLogged(logged);
            chan.setKey(key);
            channelDao.save(chan);
            ircBot.get().getUserChannelDao().getChannel(channel).send().part(Sofia.channelDeleted(getRequestedBy()));
            join(chan);
        }
    }
}
