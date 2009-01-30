package javabot;

import java.util.Date;

public class Seen {
    private String nick;
    private String channel;
    private String message;
    private Date updated;

    public Seen(final String channel, final String message, final String nick, final Date updated) {
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

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date date) {
        updated = date;
    }
}