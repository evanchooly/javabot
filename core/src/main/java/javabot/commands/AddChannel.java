package javabot.commands;

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
public class AddChannel extends BaseCommand {
    @Autowired
    private ChannelDao dao;
    @Param
    String channel;
    @Param(defaultValue = "true", required = false)
    String logged;
    @Param(defaultValue = "", required = false)
    String password;

    @Override
    public void execute(final Javabot bot, final BotEvent event) {
        Channel chan = dao.get(channel);
        final Boolean isLogged = Boolean.valueOf(logged);
        if (channel == null) {
            chan = dao.create(channel, isLogged, password);
        } else {
            chan.setLogged(isLogged);
            dao.save(chan);
        }
        bot.postMessage(new Message(event.getChannel(), event, "Now joining " + channel +
            (isLogged ? " and logging it" : "")));
        chan.join(bot);
        bot.postMessage(new Message(channel, event, "I was asked to join this channel by " + event.getSender()));
    }
}
