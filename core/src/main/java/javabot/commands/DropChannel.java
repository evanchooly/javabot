package javabot.commands;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ChannelDao;
import javabot.model.Channel;
import org.pircbotx.PircBotX;

import javax.inject.Inject;
import javax.inject.Provider;

@SPI({AdminCommand.class})
public class DropChannel extends AdminCommand {
    @Inject
    private ChannelDao dao;

    @Inject
    private Provider<PircBotX> ircBot;

    @Param
    String channel;

    @Override
    public void execute(final Message event) {
        final Channel chan = dao.get(channel);
        if (chan != null) {
            dao.delete(chan);
            getBot().postMessage(ircBot.get().getUserChannelDao().getChannel(channel), event.getUser(),
                                 Sofia.channelDeleted(event.getUser().getNick()), event.isTell());
            event.getChannel().send().part(Sofia.channelDeleted(event.getUser().getNick()));
        } else {
            getBot().postMessage(event.getChannel(), event.getUser(), Sofia.channelUnknown(channel, event.getUser().getNick()),
                                 event.isTell());
        }
    }
}