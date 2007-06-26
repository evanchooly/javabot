package javabot.dao;

import java.util.Iterator;
import java.util.List;

import javabot.dao.util.QueryParam;
import javabot.model.Channel;

@SuppressWarnings({"ConstantNamingConvention"})
public interface ChannelDao {
    String BY_NAME = "Channel.byName";
    String ALL = "Channel.all";

    List<Channel> getChannels();

    List<String> configuredChannels();

    Iterator<Channel> getIterator(QueryParam qp);

    Channel get(String name);

    Channel getChannel(String name);

    void saveOrUpdate(Channel channel);

}