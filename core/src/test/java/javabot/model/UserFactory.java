package javabot.model;

import org.pircbotx.PircBotX;
import org.pircbotx.User;

import javax.inject.Inject;
import javax.inject.Provider;

public class UserFactory {
    @Inject
    private Provider<PircBotX> ircBot;

    public User createUser(final String nick, final String login, final String host) {
        return new TestUser(nick, login, host);
    }

    private class TestUser extends User {
        public TestUser(final String nick, final String login, final String host) {
            super(ircBot.get(), ircBot.get().getUserChannelDao(), nick);
            setHostmask(host);
            setLogin(login);
        }

        @Override
        public String toString() {
            return getNick();
        }
    }
}
