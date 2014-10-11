package javabot.operations;

import com.antwerkz.maven.SPI;
import javabot.Message;
import org.pircbotx.Channel;

@SPI(BotOperation.class)
public class Magic8BallOperation extends BotOperation {
    String[] responses = {
                             "Yes",
                             "Definitely",
                             "Absolutely",
                             "I think so",
                             "I guess that would be good",
                             "I'm not really sure",
                             "If you want",
                             "Well, if you really want to",
                             "Maybe",
                             "Probably not",
                             "Not really",
                             "Sometimes",
                             "Hmm, sounds bad",
                             "No chance!",
                             "No way!",
                             "No",
                             "Absolutely not!",
                             "Definitely not!",
                             "Don't do anything I wouldn't do",
                             "Only on a Tuesday",
                             "If I tell you that I'll have to shoot you",
                             "I'm getting something about JFK, but I don't think it's relevant"
    };

    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue().toLowerCase();
        final Channel channel = event.getChannel();
        if (message.startsWith("should i ") || message.startsWith("magic8 ")) {
            getBot().postMessage(channel, event.getUser(), responses[((int) (Math.random() * responses.length))], event.isTell());
            return true;
        }
        return false;
    }
}
