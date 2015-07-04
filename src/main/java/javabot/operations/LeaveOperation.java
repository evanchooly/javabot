package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeaveOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(LeaveOperation.class);

    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        final Channel channel = event.getChannel();
        final User sender = event.getUser();
        if ("leave".equals(message.toLowerCase())) {
            if (channel.getName().equalsIgnoreCase(event.getUser().getNick())) {
                getBot().postMessageToChannel(event, Sofia.leavePrivmsg(sender.getNick()));
            } else {
                getBot().postMessageToChannel(event, Sofia.leaveChannel(event.getUser().getNick()));
                new Thread(() -> {
                    //                        ircBot.getUserChannelDao().getChannel(channel.getName());
                    try {
                        Thread.sleep(60000 * 15);
                    } catch (InterruptedException exception) {
                        log.error(exception.getMessage(), exception);
                    }
                    //                        getBot().getPircBot().joinChannel(channel);
                }).start();
            }
        }
        return false;
    }
}
