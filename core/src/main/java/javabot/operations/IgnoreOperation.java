package javabot.operations;

import java.util.List;
import java.util.Collections;

import com.antwerkz.maven.SPI;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

@SPI(BotOperation.class)
public class IgnoreOperation extends BotOperation {
    @Override
    public List<Message> handleMessage(final BotEvent event) {
//        final String message = event.getMessage();
        //Javabot bot = event.getBot();
//        final String[] parts = message.split(" ");
/*        if((parts.length == 3)
            && parts[0].equals("quit ")
            && parts[2].equals(bot.getNickPassword())) {

            //bot.addIgnore(parts[1]);
        }*/
        return Collections.emptyList();
    }
}
