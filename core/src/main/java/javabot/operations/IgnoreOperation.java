package javabot.operations;

import java.util.List;
import java.util.Collections;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;

@SPI(BotOperation.class)
public class IgnoreOperation extends BotOperation {
    @Override
    public List<Message> handleMessage(final IrcEvent event) {
//        final String message = event.getMessage();
        //PircBotJavabot bot = event.getBot();
//        final String[] parts = message.split(" ");
/*        if((parts.length == 3)
            && parts[0].equals("quit ")
            && parts[2].equals(bot.getNickPassword())) {

            //bot.addIgnore(parts[1]);
        }*/
        return Collections.emptyList();
    }
}
