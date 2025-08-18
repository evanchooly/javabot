package javabot.mocks

import org.pircbotx.PircBotX
import org.pircbotx.UserHostmask

class MockUserHostmask(
    bot: PircBotX,
    userNick: String,
    login: String = userNick,
    hostname: String = userNick,
) : UserHostmask(bot, userNick, userNick, login, hostname) {}
