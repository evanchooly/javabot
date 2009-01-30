package javabot.operations;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

public class SayOperation extends BotOperation {
    public SayOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        String message = event.getMessage();
        if (message.startsWith("say ")) {
            message = message.substring("say ".length());
            getBot().postMessage(new Message(event.getChannel(), event, message));
            return true;
        }
        return false;
    }
}