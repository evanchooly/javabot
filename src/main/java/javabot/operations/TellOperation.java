package javabot.operations;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.operations.throttle.ThrottleItem;
import javabot.operations.throttle.Throttler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ricky_clarkson
 * @author ernimril  - Throttle patch
 */
public class TellOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(TellOperation.class);
    private static final Throttler<TellInfo> throttler =
        new Throttler<TellInfo>(100, 10 * 1000);

    public TellOperation(final Javabot bot) {
        super(bot);
    }

    private static final class TellInfo implements ThrottleItem<TellInfo> {
        private final String nick;
        private final String msg;

        public TellInfo(final String nick, final String msg) {
            this.nick = nick;
            this.msg = msg;
        }

        public boolean matches(final TellInfo ti) {
            return nick.equals(ti.nick) && msg.equals(ti.msg);
        }
    }

    private static final class TellSubject {
        private final String target;
        private final String subject;

        public TellSubject(final String target, final String subject) {
            this.target = target;
            this.subject = subject;
        }

        public String getTarget() {
            return target;
        }

        public String getSubject() {
            return subject;
        }
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final String login = event.getLogin();
        final String hostname = event.getHostname();
        final String sender = event.getSender();
        final boolean isPrivateMessage = sender.equals(channel);
        boolean handled = false;
        if (isTellCommand(message)) {
            final TellSubject tellSubject = parseTellSubject(message);
            if (tellSubject == null) {
                getBot().postMessage(new Message(channel, event,
                    String.format("The syntax is: tell nick about factoid - you missed out the 'about', %s", sender)));
                handled = true;
            } else {
                String nick = tellSubject.getTarget();
                if ("me".equals(nick)) {
                    nick = sender;
                }
                final String thing = tellSubject.getSubject();
                handled = true;
                if (nick.equals(getBot().getNick())) {
                    getBot().postMessage(new Message(channel, event, "I don't want to talk to myself"));
                } else if (throttler.isThrottled(new TellInfo(nick, thing))) {
                    if (log.isDebugEnabled()) {
                        log.debug(
                            "skipping tell of " + thing + " to " + nick + ", already told " + nick + " about " + thing);
                    }
                    getBot().postMessage(new Message(channel, event, sender + ", Slow down, Speedy Gonzalez!"));
                } else if (!getBot().userIsOnChannel(nick, channel)) {
                    getBot().postMessage(new Message(channel, event, "The user " + nick + " is not on " + channel));
                } else if (isPrivateMessage && !getBot().isOnSameChannelAs(nick)) {
                    getBot()
                        .postMessage(new Message(sender, event, "I will not send a message to someone who is not on any"
                            + " of my channels."));
                } else if (thing.endsWith("++") || thing.endsWith("--")) {
                    getBot().postMessage(new Message(channel, event, "I'm afraid I can't let you do that, Dave."));
                } else {
                    handled = getBot().getResponses(channel, nick, login, hostname, thing);
                    throttler.addThrottleItem(new TellInfo(nick, thing));
                }
            }
        }
        return handled;
    }

    private TellSubject parseTellSubject(final String message) {
        if (message.startsWith("tell ")) {
            return parseLonghand(message);
        }
        return parseShorthand(message);
    }

    private TellSubject parseLonghand(final String message) {
        final String body = message.substring("tell ".length());
        final String nick = body.substring(0, body.indexOf(" "));
        final int about = body.indexOf("about ");
        if (about < 0) {
            return null;
        }
        final String thing = body.substring(about + "about ".length());
        return new TellSubject(nick, thing);
    }

    private TellSubject parseShorthand(final String message) {
        String target = message;//.substring(0, space);
        for (final String start : getBot().getStartStrings()) {
            if (target.startsWith(start)) {
                target = target.substring(start.length()).trim();
            }
        }
        final int space = target.indexOf(' ');
        return space < 0 ? null
            : new TellSubject(target.substring(0, space).trim(), target.substring(space + 1).trim());
    }

    private boolean isTellCommand(final String message) {
        return message.startsWith("tell ") || message.startsWith("~");
    }
}
