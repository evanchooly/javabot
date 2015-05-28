package javabot.commands;

import com.antwerkz.sofia.Sofia;
import com.google.inject.Provider;
import javabot.Message;
import javabot.dao.AdminDao;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import javax.inject.Inject;

public class AddAdmin extends AdminCommand {
    @Inject
    private AdminDao dao;
    @Inject
    private Provider<PircBotX> ircBot;
    @Param
    String userName;
    @Param
    String hostName;

    @Override
    public void execute(final Message event) {
        final User user = findUser(userName);
        if (user == null) {
            getJavabot().postMessageToChannel(event, Sofia.userNotFound(userName));
        } else {
            if (dao.getAdmin(user.getNick(), hostName) != null) {
                getJavabot().postMessageToChannel(event, Sofia.adminAlready(user.getNick()));
            } else {
                dao.create(user.getNick(), user.getLogin(), user.getHostmask());
                getJavabot().postMessageToChannel(event, Sofia.adminAdded(user.getNick()));
            }
        }
    }

    public User findUser(final String name) {
        return ircBot.get().getUserChannelDao().getUser(name);
    }

}
