package javabot.commands;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ChannelDao;
import javabot.model.Channel;
import org.pircbotx.PircBotX;

import javax.inject.Inject;
import javax.inject.Provider;

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
            final Message message = new Message(ircBot.get().getUserChannelDao().getChannel(channel), event.getUser(), event.getValue());
            getBot().postMessageToChannel(message, Sofia.channelDeleted(event.getUser().getNick()));
            event.getChannel().send().part(Sofia.channelDeleted(event.getUser().getNick()));
        } else {
            getBot().postMessageToChannel(event, Sofia.channelUnknown(channel, event.getUser().getNick()));
        }
    }
}