package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

import com.rickyclarkson.java.util.TypeSafeList;

/**
 * @author ricky_clarkson
 */
public class TellOperation implements BotOperation {
    /**
     * @see javabot.operations.BotOperation#handleMessage(javabot.BotEvent)
     */
    public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);

        String message = event.getMessage();
        String channel = event.getChannel();
        String login = event.getLogin();
        String hostname = event.getHostname();
        String sender = event.getSender();

        Javabot bot = event.getBot();

        if (!message.startsWith("tell "))
		return messages;

	if (message.indexOf("about")==-1)
	{
		messages.add(new Message(channel,"The syntax is: tell nick about factoid - you missed out the 'about', "+sender,false));
		return messages;
	}

        message = message.substring("tell ".length());

        String nick = message.substring(0, message.indexOf(" "));

        if (nick.equals(bot.getNick())) {
            messages.add(new Message(channel, "I don't want to talk to myself",
                false));

            return messages;
        }

        if (nick.equals("me"))
            nick = sender;

        if (sender.equals(channel)) {
            //private message

            if (bot.isOnSameChannelAs(nick)) {
                messages.add(new Message(nick, sender + " wants to tell you "
                    + "the following:", false));

                String thing = message.substring(message.indexOf("about ")
                    + "about ".length());

                messages.addAll(bot.getResponses(nick, nick, login, hostname,
                    thing));

                messages.add(new Message(sender, "I told " + nick + " about "
                    + thing + ".", false));

                return messages;
            } else
                messages.add(new Message(sender, "I will not send a message "
                    + "to someone who is " + "not on any of my " + "channels.",
                    false));

            return messages;
        }

        //channel message

        if (bot.userIsOnChannel(nick, channel)) {
            String newMessage = message.substring(message.indexOf("about ")
                + "about ".length());
            System.out.println("newMessage=" + newMessage);

            List responses = bot.getResponses(channel, nick, login, hostname,
                newMessage);

            int length = responses.size();

            for (int a = 0; a < length; a++) {
                Message next = (Message)responses.get(a);

                if (!next.getMessage().startsWith(nick)) {
                    responses.remove(a);

                    String newMessage2 = nick + ", ";

                    String javabotNick = event.getBot().getNick();

                    if (next.isAction())
                        newMessage2 += javabotNick + " ";

                    newMessage2 += next.getMessage();

                    responses.add(a, new Message(next.getDestination(),
                        newMessage2, false));
                }
            }

            messages.addAll(responses);
        } else
            messages.add(new Message(channel, "The user " + nick
                + " is not on " + channel, false));

        return messages;
    }

    public List handleChannelMessage(BotEvent event)
    {
	    	return new TypeSafeList(new ArrayList(),Message.class);
    }
}
