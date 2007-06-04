package javabot.dao;

import javabot.dao.model.ChannelConfig;

import java.util.List;

// Author: joed
// Date  : Apr 12, 2007

public interface ChannelConfigDao {

   List<ChannelConfig> getChannels() ;

   ChannelConfig getChannel(String name);


}