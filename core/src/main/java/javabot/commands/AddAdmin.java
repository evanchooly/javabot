package javabot.commands;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.AdminDao;
import javabot.operations.BotOperation;
import org.jibble.pircbot.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SPI({BotOperation.class, AdminCommand.class})
public class AddAdmin extends AdminCommand {
    @Autowired
    private AdminDao dao;
    @Param
    String userName;
    @Param
    String hostName;

    @Override
    public List<Message> execute(final Javabot bot, final BotEvent event) {
        final List<Message> responses = new ArrayList<Message>();
        final User user = findUser(bot, event, userName);
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

    private User findUser(final Javabot bot, final BotEvent event, final String name) {
        final User[] users = bot.getUsers(event.getChannel());
        User user = null;
        for (int index = 0; user == null && index < users.length; index++) {
            if (users[index].getNick().equals(name)) {
                user = users[index];
            }
        }
        return user;
    }
}
