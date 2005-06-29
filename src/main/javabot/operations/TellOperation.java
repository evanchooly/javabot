package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ricky_clarkson
 */
public class TellOperation implements BotOperation {
    private static Log log = LogFactory.getLog(TellOperation.class);
    private final String ownNick;
    private final Javabot javabot;

    public TellOperation(final String myNick, final Javabot bot) {
        ownNick = myNick;
        javabot = bot;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList< Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String login = event.getLogin();
        String hostname = event.getHostname();
        String sender = event.getSender();
        if(!message.startsWith("tell ")) {
            return messages;
        }
        if(message.indexOf("about") == -1) {
            messages
                .add(
                    new Message(channel,
                        "The syntax is: tell nick about factoid - you missed out the 'about', "
                            + sender, false));
            return messages;
        }
        message = message.substring("tell ".length());
        String nick = message.substring(0, message.indexOf(" "));
        if(nick.equals(ownNick)) {
            messages.add(new Message(channel, "I don't want to talk to myself",
                false));
            return messages;
        }
        if("me".equals(nick)) {
            nick = sender;
        }
        if(sender.equals(channel)) {
            //private message
            if(javabot.isOnSameChannelAs(nick)) {
                messages.add(new Message(nick, sender + " wants to tell you "
                    + "the following:", false));
                String thing = message.substring(message.indexOf("about ")
                    + "about ".length());
                messages.addAll(javabot.getResponses(nick, nick, login, hostname,
                    thing));
                messages.add(new Message(sender, "I told " + nick + " about "
                    + thing + ".", false));
                return messages;
            } else {
                messages.add(new Message(sender, "I will not send a message "
                    + "to someone who is " + "not on any of my " + "channels.",
                    false));
            }
            return messages;
        }
        //channel message
        if(javabot.userIsOnChannel(nick, channel)) {
            String newMessage = message.substring(message.indexOf("about ")
                + "about ".length());
            log.debug("newMessage=" + newMessage);
            List<Message> responses = javabot.getResponses(channel, nick, login, hostname,
                newMessage);
            int length = responses.size();
            for(int a = 0; a < length; a++) {
                Message next = (Message)responses.get(a);
                if(!next.getMessage().startsWith(nick)) {
                    responses.remove(a);
                    StringBuffer newMessage2 = new StringBuffer(nick);
                    newMessage2.append(", ");
                    String javabotNick = javabot.getNick();
                    if(next.isAction()) {
                        newMessage2.append(javabotNick).append(" ");
                    }
                    newMessage2.append(next.getMessage());
                    responses.add(a, new Message(next.getDestination(),
                        newMessage2.toString(), false));
                }
            }
            messages.addAll(responses);
        } else {
            messages.add(new Message(channel, "The user " + nick
                + " is not on " + channel, false));
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList< Message>();
    }
}
