package javabot.operations

import javabot.Message

public class Magic8BallOperation : BotOperation() {
    var responses = arrayOf("Yes", "Definitely", "Absolutely", "I think so", "I guess that would be good", "I'm not really sure",
          "If you want", "Well, if you really want to", "Maybe", "Probably not", "Not really", "Sometimes", "Hmm, sounds bad", "No chance!",
          "No way!", "No", "Absolutely not!", "Definitely not!", "Don't do anything I wouldn't do", "Only on a Tuesday",
          "If I tell you that I'll have to shoot you", "I'm getting something about JFK, but I don't think it's relevant")

    override fun handleMessage(event: Message): Boolean {
        val message = event.value.toLowerCase()
        if (message.startsWith("should i ") || message.startsWith("magic8 ")) {
            bot.postMessageToChannel(event, responses[((Math.random() * responses.size).toInt())])
            return true
        }
        return false
    }
}
