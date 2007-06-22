package javabot.dao;

import java.util.Iterator;
import java.util.List;

import javabot.dao.util.QueryParam;
import javabot.model.Channel;

// Author: joed
// Date  : Apr 12, 2007

public interface ChannelDao {

    List<Channel> getChannels();

    List<String> configuredChannels();

    Iterator<Channel> getIterator(QueryParam qp);

    Channel get(String name);

    Channel getChannel(String name);

    void saveOrUpdate(Channel channel);

}