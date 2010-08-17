package javabot.commands;

import java.util.List;
import java.util.ArrayList;

import com.antwerkz.maven.SPI;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ChannelDao;
import javabot.dao.util.QueryParam;
import javabot.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.StringUtils;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SPI(Command.class)
public class ListChannels extends BaseCommand {
    @Autowired
    private ChannelDao dao;

    @Override
    public void execute(final List<Message> responses, final Javabot bot, final BotEvent event) {
        final List<Channel> channels = dao.find(new QueryParam(0, Integer.MAX_VALUE));
        responses.add(new Message(event.getChannel(), event, event.getSender() + ", I'll list the channels in a"
            + " private message for you"));
        final List<String> chans = new ArrayList<String>();
        for (final Channel channel : channels) {
            chans.add(String.format("%s %s", channel.getName(), channel.getLogged() ? "(logged)" : ""));
        }
        responses.add(new Message(event.getSender(), event, StringUtils.join(chans, ", ")));
    }
}