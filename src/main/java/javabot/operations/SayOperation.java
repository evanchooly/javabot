package javabot.operations;

import javabot.Message;

public class SayOperation extends BotOperation {
    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        if (message.startsWith("say ")) {
            getBot().postMessageToChannel(event, message.substring("say ".length()));
            return true;
        }
        return false;
    }
}