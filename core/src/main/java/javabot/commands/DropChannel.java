package javabot.commands;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
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
@SPI({AdminCommand.class})
public class DropChannel extends AdminCommand {
    @Autowired
    private ChannelDao dao;
    @Param
    String channel;

    @Override
    public List<Message> execute(final Javabot bot, final IrcEvent event) {
        final List<Message> responses = new ArrayList<Message>();
        final Channel chan = dao.get(channel);
        if (chan != null) {
            dao.delete(chan);
            responses.add(new Message(channel, event, "I was asked to leave this channel by " + event.getSender()));
            bot.getPircBot().partChannel(chan.getName());
        } else {
            responses.add(new Message(event.getChannel(), event, "I'm not in " + channel + ", " + event.getSender()));
        }

        return responses;
    }
}