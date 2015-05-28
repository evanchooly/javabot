package javabot.commands;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ChannelDao;
import javabot.model.Channel;
import org.pircbotx.PircBotX;

import javax.inject.Inject;
import javax.inject.Provider;

public class AddChannel extends AdminCommand {
    @Inject
    private ChannelDao dao;
    @Inject
    private Provider<PircBotX> ircBot;

    @Param
    String name;
    @Param(defaultValue = "true", required = false)
    String logged;
    @Param(defaultValue = "", required = false)
    String password;

    @Override
    public void execute(final Message event) {
        if (name.startsWith("#")) {
            Channel channel = dao.get(name);
            final Boolean isLogged = Boolean.valueOf(logged);
            if (channel == null) {
                channel = dao.create(name, isLogged, password);
            } else {
                channel.setLogged(isLogged);
                dao.save(channel);
            }

            getBot().postMessageToChannel(event, isLogged ? Sofia.adminJoiningLoggedChannel(name) : Sofia.adminJoiningChannel(name));
            if (channel.getKey() == null) {
                ircBot.get().sendIRC().joinChannel(channel.getName());
            } else {
                ircBot.get().sendIRC().joinChannel(channel.getName(), channel.getKey());
            }

            getBot().postMessageToChannel(new Message(event, ircBot.get().getUserChannelDao().getChannel(name)),
                                          Sofia.adminJoinedChannel(event.getUser().getNick()));
        } else {
            getBot().postMessageToChannel(event, Sofia.adminBadChannelName());
        }
    }
}
