package javabot.operations;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ricky_clarkson
 * @author ernimril  - Throttle patch
 */

public class TellOperation implements BotOperation {
    private static final Log log = LogFactory.getLog(TellOperation.class);
    private final String ownNick;
    private final Javabot javabot;
    private static final int MAX_TELL_MEMORY = 100;
    private static final int THROTTLE_TIME = 10 * 1000; // 10 secs.
    private final List<TellInfo> lastTells = new ArrayList<TellInfo>(MAX_TELL_MEMORY);

    public TellOperation(final String myNick, final Javabot bot) {
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
        if (!message.startsWith("tell ")) {
            return messages;
        }
        if (message.indexOf("about") == -1) {
            messages.add(new Message(channel, "The syntax is: tell nick about factoid - you missed out the 'about', "
                    + sender, false));
            return messages;
        }
        message = message.substring("tell ".length());
        String nick = message.substring(0, message.indexOf(" "));
        if (nick.equals(ownNick)) {
            messages.add(new Message(channel, "I don't want to talk to myself", false));
            return messages;
        }
        if ("me".equals(nick)) {
            nick = sender;
        }
        if (sender.equals(channel)) {
            //private message
            if (javabot.isOnSameChannelAs(nick)) {
                messages.add(new Message(nick, sender + " wants to tell you the following:", false));
                String thing = message.substring(message.indexOf("about ") + "about ".length());
                messages.addAll(javabot.getResponses(nick, nick, login, hostname, thing));
                messages.add(new Message(sender, "I told " + nick + " about " + thing + ".", false));
                return messages;
            } else {
                messages.add(new Message(sender, "I will not send a message to someone who is not on any"
                        + " of my channels.", false));
            }
            return messages;
        }
        //channel message
        if (javabot.userIsOnChannel(nick, channel)) {
            String newMessage = message.substring(message.indexOf("about ") + "about ".length());
            if (alreadyTold(nick, newMessage)) {
                log.debug("skipping tell of " + newMessage + " to " + nick + ", already told " + message);
                messages.add(new Message(channel, event.getSender() + " ...", false));

            } else {
                addTold(nick, newMessage);
                List<Message> responses = javabot.getResponses(channel, nick, login, hostname, newMessage);
                log.debug(sender + " is telling " + nick + " about " + newMessage);
                int length = responses.size();
                for (int a = 0; a < length; a++) {
                    Message next = responses.get(a);
                    if (!next.getMessage().startsWith(nick)) {
                        responses.remove(a);
                        StringBuilder newMessage2 = new StringBuilder(nick);
                        newMessage2.append(", ");
                        String javabotNick = javabot.getNick();
                        if (next.isAction()) {
                            newMessage2.append(javabotNick).append(" ");
                        }
                        newMessage2.append(next.getMessage());
                        responses.add(a, new Message(next.getDestination(), newMessage2.toString(), false));
                    }
                }
                messages.addAll(responses);
            }

        } else {
            messages.add(new Message(channel, "The user " + nick + " is not on " + channel, false));
        }

        return messages;
    }

    private boolean alreadyTold(String nick, String msg) {
        long now = System.currentTimeMillis();
        log.debug("alreadyTold: " + nick + " " + msg);
        for (int i = 0, j = lastTells.size(); i < j; i++) {
            TellInfo ti = lastTells.get(i);
            log.debug(ti.nick + " " + ti.msg);
            if ((now - ti.getWhen() < THROTTLE_TIME) && ti.match(nick, msg))
                return true;
        }
        return false;
    }

    private void addTold(String nick, String msg) {
        TellInfo ti = new TellInfo(nick, msg);
        lastTells.add(ti);
        if (lastTells.size() > MAX_TELL_MEMORY)
            lastTells.remove(0);
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
