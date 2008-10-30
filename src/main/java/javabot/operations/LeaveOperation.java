package javabot.operations;

import javabot.BotEvent;
import javabot.ChannelControl;
import javabot.Message;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ricky_clarkson
 */
public class LeaveOperation implements BotOperation {
    private final ChannelControl channelControl;
    private static final Logger log = LoggerFactory.getLogger(LeaveOperation.class);

    public LeaveOperation(ChannelControl control) {
        channelControl = control;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();

        String message = event.getMessage();
        final String channel = event.getChannel();
        String sender = event.getSender();

        if ("leave".equals(message.toLowerCase())) {
            if (channel.equals(sender)) {
                messages.add(new Message(channel, "I cannot leave a private "
                        + "message, " + sender + ".", false));

                return messages;
            }

            messages
                    .add(new Message(channel, "I'll be back...", false));

            new Thread(new Runnable() {
                public void run() {
                    channelControl.partChannel
                            (channel, "I was asked to leave.");

                    try {
                        Thread.sleep(60000 * 15);
                        //Thread.sleep(3600 * 1000);
                    } catch (InterruptedException exception) {
                        log.error(exception.getMessage(), exception);
                    }

                    channelControl.joinChannel(channel);
                }
            }).start();

        }

        return messages;

    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
