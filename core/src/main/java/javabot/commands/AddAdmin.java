package javabot.commands;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import com.google.inject.Provider;
import javabot.Message;
import javabot.dao.AdminDao;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import javax.inject.Inject;

@SPI({AdminCommand.class})
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
            getJavabot().postMessage(event.getChannel(), event.getUser(), Sofia.userNotFound(userName), event.isTell());
        } else {
            if (dao.getAdmin(user.getNick(), hostName) != null) {
                getJavabot().postMessage(event.getChannel(), event.getUser(), Sofia.adminAlready(user.getNick()), event.isTell());
            } else {
                dao.create(user.getNick(), user.getLogin(), user.getHostmask());
                getJavabot().postMessage(event.getChannel(), event.getUser(), Sofia.adminAdded(user.getNick()), event.isTell());
            }
        }
    }

    public User findUser(final String name) {
        return ircBot.get().getUserChannelDao().getUser(name);
    }

}
