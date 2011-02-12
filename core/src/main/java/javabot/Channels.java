package javabot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Channels {
    private final Map<String, ChannelList> channels = new ConcurrentHashMap<String, ChannelList>();
    private final Map<String, IrcUser> users = new ConcurrentHashMap<String, IrcUser>();
    
    public ChannelList get(final String channel) {
        return channels.get(channel);
    }

    public ChannelList add(final String channel) {
        final ChannelList list = new ChannelList(this, channel);
        final ChannelList put = channels.put(channel, list);
        return put == null ? list : put;
    }

    public IrcUser addUser(final String nick) {
        final IrcUser user = new IrcUser(nick, null, null);
        final IrcUser added = users.put(nick, user);
        return added == null ? user : added;
    }

    public IrcUser getUser(final String name) {
        return users.get(name);
    }

    public void add(final IrcUser user) {
        users.put(user.getNick(), user);
    }
}
