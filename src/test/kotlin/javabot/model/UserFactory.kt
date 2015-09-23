package javabot.model

import javabot.Messages
import org.pircbotx.PircBotX
import org.pircbotx.User
import org.pircbotx.UserChannelDao

import javax.inject.Inject
import javax.inject.Provider

public class UserFactory {
    Inject
    private val ircBot: Provider<PircBotX>? = null
    Inject
    private val messages: Messages? = null

    public fun createUser(nick: String, login: String, host: String): User {
        return TestUser(ircBot!!.get(), ircBot.get().userChannelDao, messages, nick, login, host)
    }

}
