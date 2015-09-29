package javabot.model

import javabot.Messages
import org.pircbotx.PircBotX
import org.pircbotx.User
import javax.inject.Inject
import javax.inject.Provider

public class UserFactory {
    @Inject
    protected lateinit var ircBot: Provider<PircBotX>
    @Inject
    protected lateinit var messages: Messages

    public fun createUser(nick: String, login: String, host: String): User {
        val bot = ircBot.get()
        return TestUser(bot, bot.userChannelDao, messages, nick, login, host)
    }

}
