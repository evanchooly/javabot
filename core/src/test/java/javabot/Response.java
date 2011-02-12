package javabot;

/**
 * Created Jan 27, 2009
*
* @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
*/
public class Response {
    private final String channel;
    private final IrcUser sender;
    private final String message;

    public Response(final String target, final IrcUser user, final String msg) {
        channel = target;
        sender = user;
        message = msg;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    public IrcUser getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "Response{" +
            "channel='" + channel + '\'' +
            ", sender='" + sender + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
