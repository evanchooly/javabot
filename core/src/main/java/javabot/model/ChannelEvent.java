package javabot.model;

import javax.inject.Inject;

import com.antwerkz.sofia.Sofia;
import com.google.code.morphia.annotations.Entity;
import javabot.Javabot;
import javabot.dao.ChannelDao;

@Entity
public class ChannelEvent extends AdminEvent {
  @Inject
  private ChannelDao channelDao;
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

  public void add(Javabot bot) {
    Channel chan = channelDao.get(channel);
    chan.join(bot);
  }

  public void delete(Javabot bot) {
    Channel chan = channelDao.get(channel);
    if (chan != null) {
      channelDao.delete(chan.getId());
      chan.leave(bot, Sofia.channelDeleted(getRequestedBy()));
    }
  }

  public void update(Javabot bot) {
    Channel chan = channelDao.get(channel);
    if (chan != null) {
      chan.setLogged(logged);
      chan.setKey(key);
      channelDao.save(chan);
      chan.leave(bot, Sofia.channelUpdated());
      chan.join(bot);
    }
  }
}
