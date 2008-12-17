package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ricky_clarkson
 * @author ernimril  - Throttle patch
 */
public class TellOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(TellOperation.class);
    private static final int MAX_TELL_MEMORY = 100;
    private static final int THROTTLE_TIME = 10 * 1000; // 10 secs.
    private final List<TellInfo> lastTells = new ArrayList<TellInfo>(MAX_TELL_MEMORY);

    public TellOperation(Javabot bot) {
        super(bot);
    }

    private static final class TellInfo {
        private final long when;
        private final String nick;
        private final String msg;

        public TellInfo(String nick, String msg) {
            this.nick = nick;
            this.msg = msg;
            when = System.currentTimeMillis();
        }

        public long getWhen() {
            return when;
        }

        public boolean match(String nick, String msg) {
            return this.nick.equals(nick) && this.msg.equals(msg);
        }
    }

    private static final class TellSubject {
        private final String target;
        private final String subject;

        public TellSubject(String target, String subject) {
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

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String login = event.getLogin();
        String hostname = event.getHostname();
        String sender = event.getSender();
        boolean isPrivateMessage = sender.equals(channel);
        if (!isTellCommand(message)) {
            return messages;
        }
        TellSubject tellSubject = parseTellSubject(message);
        if (tellSubject == null) {
            messages.add(new Message(channel, event, "The syntax is: tell nick about factoid - you missed out the 'about', "
                + sender));
            return messages;
        }
        String nick = tellSubject.getTarget();
        if ("me".equals(nick)) {
            nick = sender;
        }
        String thing = tellSubject.getSubject();
        if (nick.equals(getBot().getNick())) {
            messages.add(new Message(channel, event, "I don't want to talk to myself"));
        } else if (alreadyTold(nick, thing)) {
            if (log.isDebugEnabled()) {
                log.debug("skipping tell of " + thing + " to " + nick + ", already told " + nick + " about " + thing);
            }
            messages.add(new Message(channel, event,sender + ", Slow down, Speedy Gonzalez!"));
        } else if (!getBot().userIsOnChannel(nick, channel)) {
            messages.add(new Message(channel, event,"The user " + nick + " is not on " + channel));
        } else if (isPrivateMessage && !getBot().isOnSameChannelAs(nick)) {
            messages.add(new Message(sender, event,"I will not send a message to someone who is not on any"
                + " of my channels."));
        } else if (thing.endsWith("++") || thing.endsWith("--")) {
            messages.add(new Message(channel, event,"I'm afraid I can't let you do that, Dave."));
        } else {
            List<Message> responses = getBot().getResponses(nick, nick, login, hostname, thing);
            addTold(nick, thing);
            if (isPrivateMessage) {
                messages.add(new Message(nick, event,sender + " wants to tell you the following:"));
                messages.addAll(responses);
                messages.add(new Message(sender, event,"I told " + nick + " about " + thing + "."));
            } else {
                if (log.isDebugEnabled()) {
                    log.debug(sender + " is telling " + nick + " about " + thing);
                }
                for (Message response : responses) {
                    messages.add(prependNickToReply(channel, event, nick,
                        response));
                }
            }
        }
        return messages;
    }

    private Message prependNickToReply(String channel, BotEvent event, String nick, Message response) {
        return new Message(channel, event, response.formatResponse(getBot(), nick));
    }

    private TellSubject parseTellSubject(String message) {
        if (message.startsWith("tell ")) {
            return parseLonghand(message);
        }
        return parseShorthand(message);
    }

    private TellSubject parseLonghand(String message) {
        String body = message.substring("tell ".length());
        String nick = body.substring(0, body.indexOf(" "));
        int about = body.indexOf("about ");
        if (about < 0) {
            return null;
        }
        String thing = body.substring(about + "about ".length());
        return new TellSubject(nick, thing);
    }

    private TellSubject parseShorthand(String message) {
        String target = message;//.substring(0, space);
        for (String start : getBot().getStartStrings()) {
            if (target.startsWith(start)) {
                target = target.substring(start.length()).trim();
            }
        }
        int space = target.indexOf(' ');
        return space < 0 ? null
            : new TellSubject(target.substring(0, space).trim(), target.substring(space + 1).trim());
    }

    private boolean isTellCommand(String message) {
        return message.startsWith("tell ") || message.startsWith("~");
    }

    private boolean alreadyTold(String nick, String msg) {
        long now = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("alreadyTold: " + nick + " " + msg);
        }
        for (TellInfo ti : lastTells) {
            if (log.isDebugEnabled()) {
                log.debug(ti.nick + " " + ti.msg);
            }
            if (now - ti.getWhen() < THROTTLE_TIME && ti.match(nick, msg)) {
                return true;
            }
        }
        return false;
    }

    private void addTold(String nick, String msg) {
        TellInfo ti = new TellInfo(nick, msg);
        lastTells.add(ti);
        if (lastTells.size() > MAX_TELL_MEMORY) {
            lastTells.remove(0);
        }
    }

    @Override
    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
