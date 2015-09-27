package javabot.model

import javabot.Messages
import org.pircbotx.PircBotX
import org.pircbotx.User
import javax.inject.Inject
import javax.inject.Provider

public class UserFactory {
    @Inject
    lateinit  val ircBot: Provider<PircBotX>
    @Inject
    lateinit val messages: Messages

    public fun createUser(nick: String, login: String, host: String): User {
        val bot = ircBot.get()
        return TestUser(bot, bot.userChannelDao, messages, nick, login, host)
    }

}
