package javabot.model;

import javabot.Messages;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserChannelDao;

import javax.inject.Inject;
import javax.inject.Provider;

public class UserFactory {
    @Inject
    private Provider<PircBotX> ircBot;
    @Inject
    private Messages messages;

    public User createUser(final String nick, final String login, final String host) {
        return new TestUser(ircBot.get(), ircBot.get().getUserChannelDao(), messages, nick, login, host);
    }

}
