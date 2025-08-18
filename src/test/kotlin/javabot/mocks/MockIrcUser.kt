package javabot.mocks

import org.pircbotx.PircBotX
import org.pircbotx.User

class MockIrcUser(bot: PircBotX, userNick: String) :
    User(MockUserHostmask(bot, userNick, userNick, "localhost")) {}
