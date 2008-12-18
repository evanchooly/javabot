package javabot.commands;

import java.util.List;
import java.util.ArrayList;

import javabot.dao.AdminDao;
import javabot.Message;
import javabot.Javabot;
import javabot.BotEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.jibble.pircbot.User;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class AddAdmin implements Command {
    @Autowired
    private AdminDao dao;

    @Override
    public List<Message> execute(Javabot bot, BotEvent event, List<String> args) {
        List<Message> messages = new ArrayList<Message>();
        String userName = args.get(0);
        User user = findUser(bot, event, userName);
        if (user == null) {
            messages.add(new Message(event.getChannel(), event, "That user is not on this channel: " + userName));
        } else {
            if (args.size() != 2) {
                messages.add(new Message(event.getChannel(), event, "Usage: addAdmin <user> <host>"));
            } else {
                String hostName = args.get(1);
                if (dao.getAdmin(user.getNick(), hostName) != null) {
                    messages.add(new Message(event.getChannel(), event, user.getNick() + " is already a bot admin"));
                } else {
                    dao.create(user.getNick(), null, hostName);
                    messages
                        .add(new Message(event.getChannel(), event, user.getNick() + " has been added as a bot admin"));
                }
            }
        }
        return messages;
    }

    private User findUser(Javabot bot, BotEvent event, String name) {
        User[] users = bot.getUsers(event.getChannel());
        User user = null;
        for (int index = 0; user == null && index < users.length; index++) {
            if (users[index].getNick().equals(name)) {
                user = users[index];
            }
        }
        return user;
    }
}
