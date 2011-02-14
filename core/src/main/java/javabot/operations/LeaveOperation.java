package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.IrcUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPI(BotOperation.class)
public class LeaveOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(LeaveOperation.class);

    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final IrcUser sender = event.getSender();
        final List<Message> responses = new ArrayList<Message>();
        if ("leave".equals(message.toLowerCase())) {
            if (channel.equalsIgnoreCase(sender.getNick())) {
                responses.add(new Message(channel, event, "I cannot leave a private message, " + sender + "."));
            } else {
                responses.add(new Message(channel, event, "I'll be back..."));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getBot().getPircBot().partChannel(channel);
                        try {
                            Thread.sleep(60000 * 15);
                        } catch (InterruptedException exception) {
                            log.error(exception.getMessage(), exception);
                        }
                        getBot().getPircBot().joinChannel(channel);
                    }
                }).start();
            }
        }
        return responses;
    }
}
