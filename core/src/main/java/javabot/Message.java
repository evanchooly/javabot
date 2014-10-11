package javabot;

import org.pircbotx.Channel;
import org.pircbotx.User;

import static java.lang.String.format;

public class Message {
    private final Channel channel;
    private final User user;
    private final String value;
    private final  User sender;
    private final  boolean tell;

    public Message(final Channel dest, final User user, final String value) {
        channel = dest;
        this.user = user;
        this.value = value;
        sender = null;
        tell = false;
    }

    public Message(final User user, final String value) {
        channel = null;
        this.user = user;
        this.value = value;
        sender = null;
        tell = false;
    }

    public Message(final Channel dest, final User user, final String value, final User sender) {
        channel = dest;
        this.user = user;
        this.value = value;
        this.sender = sender;
        this.tell = true;
    }

    public Message(final Message message, final String value) {
        channel = message.getChannel();
        user = message.getUser();
        this.value = value;
        tell = message.isTell();
        sender = null;
    }

    public Channel getChannel() {
        return channel;
    }

    public User getSender() {
        return sender;
    }

    public User getUser() {
        return user;
    }

    public String getValue() {
        return value;
    }

    public boolean isTell() {
        return tell;
    }

    public String resolveMessage() {
        return tell ? format("%s, %s", user.getNick(), value) : value;
    }

    @Override
    public String toString() {
        return "Message{" +
               "channel=" + channel.getName() +
               ", user=" + user.getNick() +
               ", message='" + value + '\'' +
               ", tell=" + tell +
               '}';
    }

    public User getOriginalUser() {
        return sender == null ? user : sender;
    }
}
