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
 */
public class LeaveOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(LeaveOperation.class);

    public LeaveOperation(Javabot bot) {
        super(bot);
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();

        String message = event.getMessage();
        final String channel = event.getChannel();
        String sender = event.getSender();

        if ("leave".equals(message.toLowerCase())) {
            if (channel.equals(sender)) {
                messages.add(new Message(channel, event, "I cannot leave a private message, " + sender + "."));

                return messages;
            }

            messages.add(new Message(channel, event, "I'll be back..."));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getBot().partChannel(channel, "I was asked to leave.");

                    try {
                        Thread.sleep(60000 * 15);
                    } catch (InterruptedException exception) {
                        log.error(exception.getMessage(), exception);
                    }

                    getBot().joinChannel(channel);
                }
            }).start();

        }

        return messages;

    }
}
