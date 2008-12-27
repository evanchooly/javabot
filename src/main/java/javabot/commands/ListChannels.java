package javabot.commands;

import java.util.List;
import java.util.ArrayList;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.model.Channel;
import javabot.dao.ChannelDao;
import javabot.dao.util.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class ListChannels implements Command {
    @Autowired
    private ChannelDao dao;

    @Override
    public List<Message> execute(Javabot bot, BotEvent event, List<String> args) {
        List<Message> messages = new ArrayList<Message>();
        List<Channel> channels = dao.find(new QueryParam(0, Integer.MAX_VALUE));
        messages.add(new Message(event.getChannel(), event, event.getSender() + ", I'll list the channels in a"
            + " private message for you"));
        for (Channel channel : channels) {
            messages.add(new Message(event.getSender(), event, channel.getName() +
                (channel.getLogged() ? "(logged)" : "")));
        }
        return messages;
    }
}