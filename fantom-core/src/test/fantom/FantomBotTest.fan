

class FantomBotTest : sys::Test {
    Void testConnect() {
        FantomBot bot := FantomBot.make("irc.freenode.net");
        bot.connect
       verify(bot.isConnected, "I should be connected")
    }
}