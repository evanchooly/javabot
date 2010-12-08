package javabot;

import org.schwering.irc.lib.IRCUser;

public class IrcEvent {
    private final String channel;
    private final IRCUser sender;
    private String message;

    public IrcEvent(final String eventChannel, final IRCUser user, final String eventMessage) {
        channel = eventChannel;
        sender = user;
        message = eventMessage;
    }

    public String getChannel() {
        return channel;
    }

    public IRCUser getSender() {
        return sender;
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
            ", sender='" + sender + "'" +
            ", message='" + message + "'" +
            "}";
    }
}