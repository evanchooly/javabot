package javabot.dao;

import javabot.dao.model.ChannelConfig;
import javabot.dao.util.QueryParam;

import java.util.Iterator;
import java.util.List;

// Author: joed
// Date  : Apr 12, 2007

public interface ChannelConfigDao {

    List<ChannelConfig> getChannels();

    Iterator<ChannelConfig> getIterator(QueryParam qp);

    ChannelConfig get(String name);

    ChannelConfig getChannel(String name);

    void saveOrUpdate(ChannelConfig channel);

}