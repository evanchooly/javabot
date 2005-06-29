package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.ChannelControl;
import javabot.Message;

import com.rickyclarkson.java.util.TypeSafeList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ricky_clarkson
 */
public class LeaveOperation implements BotOperation {
    private final ChannelControl channelControl;
    private static Log log = LogFactory.getLog(LeaveOperation.class);

    public LeaveOperation(final ChannelControl channelControl)
    {
        this.channelControl=channelControl;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList< Message>();

        final String message = event.getMessage();
        final String channel = event.getChannel();
        final String sender = event.getSender();

        if ("leave".equals(message.toLowerCase())) {
            if (channel.equals(sender)) {
                messages.add(new Message(channel, "I cannot leave a private "
                    + "message, " + sender + ".", false));

                return messages;
            }

            messages
                .add(new Message(channel, "I'll be back in one hour", false));

            new Thread(new Runnable() {
                public void run() {
                    channelControl.partChannel
                (channel, "I was asked to leave.");

                    try {
                        Thread.sleep(3600 * 1000);
                    } catch (Exception exception) {
                        log.error(exception.getMessage(), exception);
                    }

                    channelControl.joinChannel(channel);
                }
            }).start();

        }

        return messages;

    }

    public List<Message> handleChannelMessage(BotEvent event)
    {
            return new ArrayList<Message>();
    }
}
