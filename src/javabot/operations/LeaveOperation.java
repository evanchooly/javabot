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
public class LeaveOperation implements BotOperation {
    /**
     * @see javabot.operations.BotOperation#handleMessage(javabot.BotEvent)
     */
    public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);

        final String message = event.getMessage();
        final String channel = event.getChannel();
        final String sender = event.getSender();
        final Javabot bot = event.getBot();

        if (message.toLowerCase().equals("leave")) {
            if (channel.equals(sender)) {
                messages.add(new Message(channel, "I cannot leave a private "
                    + "message, " + sender + ".", false));

                return messages;
            }

            messages
                .add(new Message(channel, "I'll be back in one hour", false));

            new Thread(new Runnable() {
                public void run() {
                    bot.partChannel(channel, "I was asked to leave.");

                    try {
                        Thread.sleep(3600 * 1000);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    bot.joinChannel(channel);
                }
            }).start();

        }

        return messages;

    }
}