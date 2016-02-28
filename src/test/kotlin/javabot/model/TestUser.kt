package javabot.model

import javabot.Messages
import org.pircbotx.Channel
import org.pircbotx.PircBotX
import org.pircbotx.User
import org.pircbotx.UserChannelDao
import org.pircbotx.output.OutputUser

class TestUser(private val ircBot: PircBotX,
                      userChannelDao: UserChannelDao<User, Channel>,
                      private val messages: Messages,
                      nick: String,
                      login: String?,
                      host: String?) : User(ircBot, userChannelDao, nick) {

    init {
        hostmask = host
        setLogin(login)
    }

    override fun send(): OutputUser {
        return object : OutputUser(ircBot, this@TestUser) {
            override fun message(message: String) {
                messages.add(message)
            }
        }
    }

    override fun toString(): String {
        return nick
    }
}
