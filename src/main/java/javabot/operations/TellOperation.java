package javabot.operations;

import java.text.MessageFormat;
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
public class TellOperation implements BotOperation {
    private static final Logger log = LoggerFactory.getLogger(TellOperation.class);
    private final String ownNick;
    private final Javabot javabot;
    private static final int MAX_TELL_MEMORY = 100;
    private static final int THROTTLE_TIME = 10 * 1000; // 10 secs.
    private final List<TellInfo> lastTells = new ArrayList<TellInfo>(MAX_TELL_MEMORY);

    public TellOperation(String myNick, Javabot bot) {
        ownNick = myNick;
        javabot = bot;
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
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String login = event.getLogin();
        String hostname = event.getHostname();
        String sender = event.getSender();
        boolean isPrivateMessage = sender.equals(channel);
        if(!isTellCommand(message)) {
            return messages;
        }
        TellSubject tellSubject = parseTellSubject(message);
        if(tellSubject == null) {
            messages.add(new Message(channel, "The syntax is: tell nick about factoid - you missed out the 'about', "
                + sender, false));
            return messages;
        }
        String nick = tellSubject.getTarget();
        if("me".equals(nick)) {
            nick = sender;
        }
        String thing = tellSubject.getSubject();
        if(nick.equals(ownNick)) {
            messages.add(new Message(channel, "I don't want to talk to myself", false));
        } else if(alreadyTold(nick, thing)) {
            if(log.isDebugEnabled()) {
                log.debug("skipping tell of " + thing + " to " + nick + ", already told " + nick + " about " + thing);
            }
            messages.add(new Message(channel, sender + ", Slow down, Speedy Gonzalez!", false));
        } else if(!javabot.userIsOnChannel(nick, channel)) {
            messages.add(new Message(channel, "The user " + nick + " is not on " + channel, false));
        } else if(isPrivateMessage && !javabot.isOnSameChannelAs(nick)) {
            messages.add(new Message(sender, "I will not send a message to someone who is not on any"
                + " of my channels.", false));
        } else if(thing.endsWith("++") || thing.endsWith("--")) {
            messages.add(new Message(channel, "I'm afraid I can't let you do that, Dave.", false));
        } else {
            List<Message> responses = javabot.getResponses(nick, nick, login, hostname, thing);
            addTold(nick, thing);
            if(isPrivateMessage) {
                messages.add(new Message(nick, sender + " wants to tell you the following:", false));
                messages.addAll(responses);
                messages.add(new Message(sender, "I told " + nick + " about " + thing + ".", false));
            } else {
                if(log.isDebugEnabled()) {
                    log.debug(sender + " is telling " + nick + " about " + thing);
                }
                for(Message response : responses) {
                    messages.add(prependNickToReply(channel, nick,
                        response));
                }
            }
        }
        return messages;
    }

    private Message prependNickToReply(String channel, String nick,
        Message response) {
        String reply = response.getMessage();
        if(!reply.startsWith(nick)) {
            if(response.isAction()) {
                reply = MessageFormat.format("{0}, {1} {2}", nick, javabot.getNick(), reply);
            } else {
                reply = MessageFormat.format("{0}, {1}", nick, reply);
            }
        }
        return new Message(channel, reply, false);
    }

    private TellSubject parseTellSubject(String message) {
        if(message.startsWith("tell ")) {
            return parseLonghand(message);
        }
        return parseShorthand(message);
    }

    private TellSubject parseLonghand(String message) {
        message = message.substring("tell ".length());
        String nick = message.substring(0, message.indexOf(" "));
        int about = message.indexOf("about ");
        if(about < 0) {
            return null;
        }
        String thing = message.substring(about + "about ".length());
        return new TellSubject(nick, thing);
    }

    private TellSubject parseShorthand(String message) {
        int space = message.indexOf(' ');
        if(space < 0) {
            return null;
        }
        return new TellSubject(message.substring(1, space), message.substring(space + 1));
    }

    private boolean isTellCommand(String message) {
        return message.startsWith("tell ") || message.startsWith("~");
    }

    private boolean alreadyTold(String nick, String msg) {
        long now = System.currentTimeMillis();
        if(log.isDebugEnabled()) {
            log.debug("alreadyTold: " + nick + " " + msg);
        }
        for(int i = 0, j = lastTells.size(); i < j; i++) {
            TellInfo ti = lastTells.get(i);
            if(log.isDebugEnabled()) {
                log.debug(ti.nick + " " + ti.msg);
            }
            if(now - ti.getWhen() < THROTTLE_TIME && ti.match(nick, msg)) {
                return true;
            }
        }
        return false;
    }

    private void addTold(String nick, String msg) {
        TellInfo ti = new TellInfo(nick, msg);
        lastTells.add(ti);
        if(lastTells.size() > MAX_TELL_MEMORY) {
            lastTells.remove(0);
        }
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
