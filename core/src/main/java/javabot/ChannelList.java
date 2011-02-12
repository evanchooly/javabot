package javabot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelList {
    private final String channel;
    private final Channels channels;
    private final Map<String, IrcUser> users = new ConcurrentHashMap<String, IrcUser>();

    public ChannelList(final Channels channels, final String channel) {
        this.channels = channels;
        this.channel = channel;
    }

    public void addUser(final String nick) {
        users.put(nick, channels.addUser(nick));
    }

    public IrcUser getUser(final String nick) {
        return users.get(nick);
    }
}
