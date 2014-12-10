package javabot.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryImpl;
import javabot.model.Activity;
import javabot.dao.util.QueryParam;
import javabot.model.Channel;
import javabot.model.criteria.ChannelCriteria;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

@SuppressWarnings({"ConstantNamingConvention"})
public class ChannelDao extends BaseDao<Channel> {
  public ChannelDao() {
    super(Channel.class);
  }

  public void delete(final ObjectId id) {
    delete(find(id));
  }

  @SuppressWarnings({"unchecked"})
  public List<String> configuredChannels() {
    return ((QueryImpl) getQuery()).getCollection().distinct("name");
  }

  @SuppressWarnings({"unchecked"})
  public List<Channel> getChannels() {
    return getChannels(false);
  }

  public List<Channel> getChannels(Boolean showAll) {
    ChannelCriteria channelCriteria = new ChannelCriteria(ds);
    if (!showAll) {
      channelCriteria.logged().equal(true);
    }
    Query<Channel> query = channelCriteria.query();
    query.order("name");
    return query.asList();
  }

  @SuppressWarnings({"unchecked"})
  public List<Channel> find(final QueryParam qp) {
    String condition = qp.getSort();
    if (!qp.isSortAsc()) {
      condition = "-" + condition;
    }
    return getQuery().order(condition).asList();
  }

  public boolean isLogged(final String channel) {
    final Channel chan = get(channel);
    return chan != null ? chan.getLogged() : Boolean.FALSE;
  }

  public Channel get(final String name) {
    ChannelCriteria criteria = new ChannelCriteria(ds);
    criteria.upperName().equal(name.toUpperCase());
    return criteria.query().get();
  }

  @SuppressWarnings({"unchecked"})
  public List<Activity> getStatistics() {
/*
@NamedQuery(name = ChannelDao.STATISTICS, query = "select new javabot.model.Activity(l.channel, count(l), max(l.updated),"
    + " min(l.updated), (select count(e) from Logs e)) from Logs l "
    + "where l.channel like '#%' group by l.channel order by count(l) desc")
*/
    return null;//(List<Activity>) getEntityManager().createNamedQuery(ChannelDao.STATISTICS)
//            .getResultList();
  }

  @SuppressWarnings({"unchecked"})
  public List<String> loggedChannels() {
    ChannelCriteria criteria = new ChannelCriteria(ds);
    criteria.logged().equal(true);
    List<Channel> channels = criteria.query().retrievedFields(true, "name").asList();
    List<String> names = new ArrayList<>();
    for (Channel channel : channels) {
      names.add(channel.getName());
    }
    return names;
  }

  public Channel create(final String name, final Boolean logged, final String key) {
    final Channel channel = new Channel();
    channel.setName(name);
    channel.setLogged(logged == null ? Boolean.TRUE : logged);
    channel.setKey(!StringUtils.isEmpty(key) ? key : null);
    save(channel);
    return channel;
  }

  public void save(final Channel channel) {
    channel.setUpdated(LocalDateTime.now());
    super.save(channel);
  }

}