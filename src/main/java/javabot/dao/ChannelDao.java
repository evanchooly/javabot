package javabot.dao;

import java.util.List;

import javabot.dao.util.QueryParam;
import javabot.model.Channel;

@SuppressWarnings({"ConstantNamingConvention"})
public interface ChannelDao extends BaseDao<Channel> {
    String BY_NAME = "Channel.byName";
    String ALL = "Channel.all";
    String CONFIGURED_CHANNELS = "Channel.configure";

    List<Channel> getChannels();

    List<String> configuredChannels();

    List<Channel> find(QueryParam qp);

    Channel get(String name);

    Channel create(String name, Boolean logged);

    void save(Channel channel);

    boolean isLogged(String channel);
}