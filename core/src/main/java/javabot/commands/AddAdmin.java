package javabot.commands;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.AdminDao;
import org.schwering.irc.lib.IrcUser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SPI({AdminCommand.class})
public class AddAdmin extends AdminCommand {
    @Autowired
    private AdminDao dao;
    @Param
    String userName;
    @Param
    String hostName;

    @Override
    public List<Message> execute(final Javabot bot, final IrcEvent event) {
        final List<Message> responses = new ArrayList<Message>();
        final IrcUser user = findUser(bot, event, userName);
        if (user == null) {
            responses.add(new Message(event.getChannel(), event, "That user is not on this channel: " + userName));
        } else {
            if (dao.getAdmin(user.getNick(), hostName) != null) {
                responses.add(new Message(event.getChannel(), event, user.getNick() + " is already a bot admin"));
            } else {
                dao.create(user.getNick(), hostName);
                responses.add(
                    new Message(event.getChannel(), event, user.getNick() + " has been added as a bot admin"));
            }
        }
        return responses;
    }

    private IrcUser findUser(final Javabot bot, final IrcEvent event, final String name) {
/*
        final Collection<ChannelUser> users = bot.getChannels().get(event.getChannel()).getChannelUsers();
        for (final ChannelUser user : users) {
            if (user.getNick().equals(name)) {
                return bot.getUser(name);
            }
        }
*/
        return null;
    }
}
