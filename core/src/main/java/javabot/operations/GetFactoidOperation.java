package javabot.operations;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

import javabot.Action;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.TellMessage;
import javabot.operations.throttle.Throttler;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GetFactoidOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(GetFactoidOperation.class);
    private static final Throttler<TellInfo> throttler = new Throttler<TellInfo>(100, Javabot.THROTTLE_TIME);
    @Autowired
    private FactoidDao factoidDao;

    public GetFactoidOperation(final Javabot bot) {
        super(bot);
    }

    @Override
    public List<Message> handleMessage(final BotEvent event) {
        List<Message> responses = tell(event);
        if (responses.isEmpty()) {
            responses = new ArrayList<Message>();
            responses.add(getFactoid(null, event.getMessage(), event.getSender(), event.getChannel(), event,
                new HashSet<String>()));
        }
        return responses;
    }

    private Message getFactoid(final TellSubject subject, final String toFind, final String sender, final String channel,
        final BotEvent event, final Set<String> backtrack) {
        String message = toFind;
        if (log.isDebugEnabled()) {
            log.debug(sender + " : " + message);
        }
        if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
            message = message.substring(0, message.length() - 1);
        }
        final String firstWord = message.split(" ")[0];
        String params = message.substring(firstWord.length()).trim();
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
        return factoid != null
            ? getResponse(subject, sender, channel, event, backtrack, params, factoid)
            : new Message(channel, event, sender + ", I have no idea what " + toFind + " is.");
    }

    private Message getResponse(final TellSubject subject, final String sender, final String channel,
        final BotEvent event, final Set<String> backtrack, final String replacedValue, final Factoid factoid) {
        String message = factoid.evaluate(subject, sender, replacedValue);
        if (message.startsWith("<see>")) {
            if (backtrack.contains(message)) {
                return new Message(channel, event, "Loop detected for factoid '" + message + "'.");
            } else {
                backtrack.add(message);
                return getFactoid(subject, message.substring(5).trim(), sender, channel, event, backtrack);
            }
        } else if (message.startsWith("<reply>")) {
            return new Message(channel, event, message.substring("<reply>".length()));
        } else if (message.startsWith("<action>")) {
            return new Action(channel, event, message.substring("<action>".length()));
        } else {
            return new Message(channel, event, message);
        }
    }

    private List<Message> tell(final BotEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final String sender = event.getSender();
        List<Message> responses = new ArrayList<Message>();
        if (isTellCommand(message)) {
            final TellSubject tellSubject = parseTellSubject(message);
            if (tellSubject == null) {
                responses.add(new Message(channel, event,
                    String.format("The syntax is: tell nick about factoid - you missed out the 'about', %s", sender)));
            } else {
                String nick = tellSubject.getTarget();
                if ("me".equals(nick)) {
                    nick = sender;
                }
                final String thing = tellSubject.getSubject();
                if (nick.equals(getBot().getNick())) {
                    responses.add(new Message(channel, event, "I don't want to talk to myself"));
                } else {
                    final TellInfo info = new TellInfo(nick, thing);
                    if (throttler.isThrottled(info)) {
                        if (log.isDebugEnabled()) {
                            log.debug(String.format("I already told %s about %s.", nick, thing));
                        }
                        responses.add(new Message(channel, event, sender + ", Slow down, Speedy Gonzalez!"));
                    } else if (!getBot().userIsOnChannel(nick, channel)) {
                        responses.add(new Message(channel, event, "The user " + nick + " is not on " + channel));
                    } else if (sender.equals(channel) && !getBot().isOnSameChannelAs(nick)) {
                        responses.add(new Message(sender, event, "I will not send a message to someone who is not on any"
                                    + " of my channels."));
                    } else if (thing.endsWith("++") || thing.endsWith("--")) {
                        responses.add(new Message(channel, event, "I'm afraid I can't let you do that, Dave."));
                    } else {
                        final List<Message> list = getBot()
                            .getResponses(channel, sender, event.getLogin(), event.getHostname(), thing);
                        for (Message msg : list) {
                            responses.add(new TellMessage(nick, msg.getDestination(), msg.getEvent(), msg.getMessage()));
                        }
                        throttler.addThrottleItem(info);
                    }
                }
            }
        }
        return responses;
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