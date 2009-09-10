package javabot.commands;

import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ChannelDao;
import javabot.dao.util.QueryParam;
import javabot.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class ListChannels extends BaseCommand {
    @Autowired
    private ChannelDao dao;

    @Override
    public void execute(final Javabot bot, final BotEvent event) {
        final List<Channel> channels = dao.find(new QueryParam(0, Integer.MAX_VALUE));
        bot.postMessage(new Message(event.getChannel(), event, event.getSender() + ", I'll list the channels in a"
            + " private message for you"));
        for (final Channel channel : channels) {
            bot.postMessage(new Message(event.getSender(), event,
                String.format("%s%s", channel.getName(), channel.getLogged() ? "(logged)" : "")));
        }
    }
}