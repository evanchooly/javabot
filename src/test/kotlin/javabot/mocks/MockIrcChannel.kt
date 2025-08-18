package javabot.mocks

import org.pircbotx.Channel
import org.pircbotx.PircBotX

class MockIrcChannel(bot: PircBotX, channelName: String) : Channel(bot, channelName) {}
