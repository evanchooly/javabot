package javabot;

import java.time.LocalDateTime;

public class Seen {
    private String nick;
    private String channel;
    private String message;
    private LocalDateTime updated;

    public Seen(final String channel, final String message, final String nick, final LocalDateTime updated) {
        this.channel = channel;
        this.message = message;
        this.nick = nick;
        this.updated = updated;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String name) {
        nick = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channelName) {
        channel = channelName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String seenMessage) {
        message = seenMessage;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime date) {
        updated = date;
    }
}