package javabot.commands;

import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ChannelDao;
import javabot.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class DropChannel extends BaseCommand {
    @Autowired
    private ChannelDao dao;
    @Param
    String channel;

    @Override
    public void execute(final List<Message> responses, final Javabot bot, final BotEvent event) {
        final Channel chan = dao.get(this.channel);
        if (chan != null) {
            dao.delete(chan);
            responses.add(new Message(this.channel, event, "I was asked to leave this channel by "
                + event.getSender()));
            bot.partChannel(chan.getName());
        } else {
            responses.add(new Message(event.getChannel(), event, "I'm not in " + this.channel
                + ", " + event.getSender()));
        }
    }
}