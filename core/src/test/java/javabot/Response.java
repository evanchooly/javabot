package javabot;

import org.schwering.irc.lib.IRCUser;

/**
 * Created Jan 27, 2009
*
* @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
*/
public class Response {
    private final String channel;
    private final IRCUser sender;
    private final String message;

    public Response(final String target, final IRCUser user, final String msg) {
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

    public IRCUser getSender() {
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
