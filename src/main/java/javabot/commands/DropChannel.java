package javabot.commands;

import java.util.List;
import java.util.ArrayList;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.model.Channel;
import javabot.dao.ChannelDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class DropChannel implements Command {
    @Autowired
    private ChannelDao dao;

    @Override
    public List<Message> execute(Javabot bot, BotEvent event, List<String> args) {
        List<Message> messages = new ArrayList<Message>();
        if (args.isEmpty()) {
            messages.add(new Message(event.getChannel(), event, "usage: dropChannel <channel>"));
        } else {
            String channelName = args.remove(0);
            Channel channel = dao.get(channelName);
            if (channel != null) {
                dao.delete(channel);
                messages
                    .add(new Message(channelName, event, "I was asked to leave this channel by " + event.getSender()));
                bot.partChannel(channel.getName());
            } else {
                messages.add(new Message(event.getChannel(), event, "I'm not in " + channelName
                    + ", " + event.getSender()));
            }
        }
        return messages;
    }
}