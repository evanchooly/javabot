package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;

public class IgnoreOperation extends BotOperation {
    public IgnoreOperation(Javabot javabot) {
        super(javabot);
    }

    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        //Javabot bot = event.getBot();
        String[] parts = message.split(" ");
/*        if((parts.length == 3)
            && parts[0].equals("quit ")
            && parts[2].equals(bot.getNickPassword())) {

            //bot.addIgnore(parts[1]);
        }*/
        return messages;
    }
}
