package javabot.model

import javabot.Messages
import javabot.MockUserHostmask
import org.pircbotx.PircBotX
import org.pircbotx.User
import org.pircbotx.output.OutputUser

class TestUser(
    private val ircBot: PircBotX,
    private val messages: Messages,
    nick: String,
    login: String,
    host: String
) : User(MockUserHostmask(ircBot, nick, login, host)) {

    override fun send(): OutputUser {
        return object : OutputUser(ircBot, this@TestUser) {
            override fun message(message: String) {
                messages.add(message)
            }
        }
    }
}
