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
public class AddChannel implements Command {
    @Autowired
    private ChannelDao dao;

    @Override
    public List<Message> execute(Javabot bot, BotEvent event, List<String> args) {
        List<Message> messages = new ArrayList<Message>();
        if (args.isEmpty()) {
            messages.add(new Message(event.getChannel(), event, "usage: addChannel <channel> (logged)"));
        } else {
            String channelName = args.remove(0);
            Boolean logged = !args.isEmpty() && "logged".equals(args.remove(0));
            Channel channel = dao.get(channelName);
            if (channel == null) {
                channel = dao.create(channelName, logged);
            } else {
                channel.setLogged(logged);
                dao.save(channel);
            }
            messages.add(new Message(event.getChannel(), event, "Now joining " + channelName +
                (logged ? " and logging it" : "")));
            bot.joinChannel(channel.getName());
            messages.add(new Message(channelName, event, "I was asked to join this channel by " + event.getSender()));
        }
        return messages;
    }
}
