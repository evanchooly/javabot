package javabot.dao;

import javabot.dao.model.Channel;
import javabot.dao.util.QueryParam;

import java.util.Iterator;
import java.util.List;

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