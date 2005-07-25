package javabot;

public class BotEvent {
    private String channel;
    private String sender;
    private String login;
    private String hostname;
    private String message;

    public BotEvent(String eventChannel, String user, String loginName,
        String host, String eventMessage) {
        channel = eventChannel;
        sender = user;
        login = loginName;
        hostname = host;
        message = eventMessage;
    }

    public String getChannel() {
        return channel;
    }

    public String getSender() {
        return sender;
    }

    public String getLogin() {
        return login;
    }

    public String getHostname() {
        return hostname;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return "BotEvent{" +
            "channel='" + channel + "'" +
            ", sender='" + sender + "'" +
            ", login='" + login + "'" +
            ", hostname='" + hostname + "'" +
            ", message='" + message + "'" +
            "}";
    }
}