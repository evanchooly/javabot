package javabot.operations;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import javabot.Action;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.operations.throttle.Throttler;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GetFactoidOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(GetFactoidOperation.class);
    private static final Throttler<TellInfo> throttler = new Throttler<TellInfo>(100, 5 * 1000);
    @Autowired
    private FactoidDao factoidDao;

    public GetFactoidOperation(final Javabot bot) {
        super(bot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        if (!tell(event)) {
            getFactoid(null, event.getMessage(), event.getSender(), event.getChannel(), event, new HashSet<String>());
        }
        return true;
    }

    private void getFactoid(final TellSubject subject, final String toFind, final String sender, final String channel, final BotEvent event,
        final Set<String> backtrack) {
        String message = toFind;
        if (log.isDebugEnabled()) {
            log.debug(sender + " : " + message);
        }
        if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
            message = message.substring(0, message.length() - 1);
        }
        final String firstWord = message.split(" ")[0];
        String params = message.substring(firstWord.length()).trim();
        final String key = message;
        Factoid factoid = factoidDao.getFactoid(message.toLowerCase());
        if (factoid == null) {
            factoid = factoidDao.getFactoid(firstWord + " $1");
        }
        if (factoid == null) {
            factoid = factoidDao.getFactoid(firstWord + " $+");
        }
        if (factoid == null) {
            factoid = factoidDao.getFactoid(firstWord + " $^");
        }
        if (factoid != null) {
            sendFactoid(subject, sender, channel, event, backtrack, params, key, factoid);
        } else {
            getBot().postMessage(new Message(channel, event, sender + ", I have no idea what " + toFind + " is."));
        }
    }

    private void sendFactoid(final TellSubject subject, final String sender, final String channel, final BotEvent event,
        final Set<String> backtrack, final String replacedValue, final String key, final Factoid factoid) {
        String message = factoid.evaluate(subject, sender, replacedValue);
        if (message.startsWith("<see>")) {
            if (backtrack.contains(message)) {
                getBot().postMessage(new Message(channel, event, "Loop detected for factoid '" + message + "'."));
            } else {
                backtrack.add(message);
                getFactoid(subject, message.substring("<see>".length()).trim(), sender, channel, event, backtrack);
            }
        } else if (message.startsWith("<reply>")) {
            getBot().postMessage(new Message(channel, event, message.substring("<reply>".length())));
        } else if (message.startsWith("<action>")) {
            getBot().postAction(new Action(channel, event, message.substring("<action>".length())));
        } else {
            getBot().postMessage(new Message(channel, event, message));
        }
    }

    private boolean tell(final BotEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final String sender = event.getSender();
        boolean handled = false;
        if (isTellCommand(message)) {
            final TellSubject tellSubject = parseTellSubject(message);
            handled = true;
            if (tellSubject == null) {
                getBot().postMessage(new Message(channel, event,
                    String.format("The syntax is: tell nick about factoid - you missed out the 'about', %s", sender)));
            } else {
                String nick = tellSubject.getTarget();
                if ("me".equals(nick)) {
                    nick = sender;
                }
                final String thing = tellSubject.getSubject();
                handled = true;
                if (nick.equals(getBot().getNick())) {
                    getBot().postMessage(new Message(channel, event, "I don't want to talk to myself"));
                } else {
                    final TellInfo info = new TellInfo(nick, thing);
                    if (throttler.isThrottled(info)) {
                        if (log.isDebugEnabled()) {
                            log.debug(String.format("I already told %s about %s.", nick, thing));
                        }
                        getBot().postMessage(new Message(channel, event, sender + ", Slow down, Speedy Gonzalez!"));
                    } else if (!getBot().userIsOnChannel(nick, channel)) {
                        getBot().postMessage(new Message(channel, event, "The user " + nick + " is not on " + channel));
                    } else if (sender.equals(channel) && !getBot().isOnSameChannelAs(nick)) {
                        getBot()
                            .postMessage(new Message(sender, event, "I will not send a message to someone who is not on any"
                                + " of my channels."));
                    } else if (thing.endsWith("++") || thing.endsWith("--")) {
                        getBot().postMessage(new Message(channel, event, "I'm afraid I can't let you do that, Dave."));
                    } else {
                        getFactoid(tellSubject, thing, event.getLogin(), channel, event, new HashSet<String>());
                        throttler.addThrottleItem(info);
                    }
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