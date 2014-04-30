package javabot;

import javabot.model.IrcUser;

public class IrcEvent {
    private final String channel;
    private final IrcUser sender;
    private String message;

    public IrcEvent(final String eventChannel, final IrcUser user, final String eventMessage) {
        channel = eventChannel;
        sender = user;
        message = eventMessage;
    }

    public String getChannel() {
        return channel;
    }

    public IrcUser getSender() {
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