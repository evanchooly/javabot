package javabot.model;

import javabot.Messages;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserChannelDao;
import org.pircbotx.output.OutputUser;

import javax.inject.Inject;

public class TestUser extends User {
    private final PircBotX ircBot;
    private Messages messages;

    public TestUser(final PircBotX ircBot,
                    final UserChannelDao userChannelDao,
                    final Messages messages,
                    final String nick,
                    final String login,
                    final String host) {
        super(ircBot, userChannelDao, nick);
        this.ircBot = ircBot;
        this.messages = messages;
        setHostmask(host);
        setLogin(login);
    }

    @Override
    public OutputUser send() {
        return new OutputUser(ircBot, TestUser.this) {
            @Override
            public void message(final String message) {
                messages.add(message);
            }
        };
    }

    @Override
    public String toString() {
        return getNick();
    }
}
