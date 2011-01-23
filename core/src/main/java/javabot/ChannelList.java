package javabot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.schwering.irc.lib.IrcUser;

public class ChannelList {
    private String channel;
    private Channels channels;
    private final Map<String, IrcUser> users = new ConcurrentHashMap<String, IrcUser>();

    public ChannelList(final String channel) {
        this.channel = channel;
    }

    public void addUser(final String nick) {
        users.put(nick, channels.addUser(nick));
    }

    public IrcUser getUser(final String nick) {
        return users.get(nick);
    }
}
