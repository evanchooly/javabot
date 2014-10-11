package javabot;

import javabot.model.IrcUser;
import org.pircbotx.Channel;
import org.pircbotx.User;

public class IrcEvent {
    private final Channel channel;
    private final User user;
    private String message;

    public IrcEvent(final Channel eventChannel, final User user, final String eventMessage) {
        channel = eventChannel;
        this.user = user;
        message = eventMessage;
    }

    public Channel getChannel() {
        return channel;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BotEvent{" +
            "channel='" + channel + "'" +
            ", sender='" + user + "'" +
            ", message='" + message + "'" +
            "}";
    }
}