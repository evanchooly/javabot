package javabot.operations;

import javabot.BotEvent;
import javabot.Javabot;

public class IgnoreOperation extends BotOperation {
    public IgnoreOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        final String message = event.getMessage();
        //Javabot bot = event.getBot();
        final String[] parts = message.split(" ");
/*        if((parts.length == 3)
            && parts[0].equals("quit ")
            && parts[2].equals(bot.getNickPassword())) {

            //bot.addIgnore(parts[1]);
        }*/
        return false;
    }
}
