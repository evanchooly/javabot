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
    private static final String LOGGED = "logged";

    @Override
    public List<Message> execute(Javabot bot, BotEvent event, List<String> args) {
        List<Message> messages = new ArrayList<Message>();
        if (args.isEmpty()) {
            messages.add(new Message(event.getChannel(), event, "usage: addChannel <channel> ("
                + LOGGED + ") (password)"));
            messages.add(new Message(event.getChannel(), event,
                "usage: the password and 'logged' are optional and can appear in any order"));
        } else {
            String channelName = args.remove(0);
            Boolean logged = null;
            String key = null;
            while (!args.isEmpty()) {
                String next = args.remove(0);
                logged = logged == null && LOGGED.equals(next);
                if(!LOGGED.equals(next) && key == null) {
                    key = next;
                }
            }
            Channel channel = dao.get(channelName);
            if (channel == null) {
                channel = dao.create(channelName, logged, key);
            } else {
                channel.setLogged(logged);
                dao.save(channel);
            }
            messages.add(new Message(event.getChannel(), event, "Now joining " + channelName +
                (logged ? " and logging it" : "")));
            channel.join(bot);
            messages.add(new Message(channelName, event, "I was asked to join this channel by " + event.getSender()));
        }
        return messages;
    }
}
