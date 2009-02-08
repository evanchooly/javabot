package javabot;

/**
 * Created Jan 27, 2009
*
* @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
*/
public class Response {
    private final String channel;
    private final String sender;
    private final String login;
    private final String hostname;
    private final String message;

    public Response(final String channel, final String sender, final String login,
        final String hostname, final String message) {
        this.channel = channel;
        this.sender = sender;
        this.login = login;
        this.hostname = hostname;
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public String getHostname() {
        return hostname;
    }

    public String getLogin() {
        return login;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "Response{" +
            "channel='" + channel + '\'' +
            ", sender='" + sender + '\'' +
            ", login='" + login + '\'' +
            ", hostname='" + hostname + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
