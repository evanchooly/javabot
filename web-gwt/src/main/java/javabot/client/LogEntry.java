package javabot.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LogEntry implements IsSerializable {
    private String nick;
    private String channel;
    private String message;
    private String time;
    private String type;

    public String getNick() {
        return nick;
    }

    public void setNick(final String user) {
        nick = user;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(final String chanName) {
        channel = chanName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String logMessage) {
        message = logMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(final String date) {
        time = date;
    }

    public String getType() {
        return type;
    }

    public void setType(final String value) {
        type = value;
    }
}
