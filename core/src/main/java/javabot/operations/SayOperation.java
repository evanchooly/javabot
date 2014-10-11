package javabot.operations;

import com.antwerkz.maven.SPI;
import javabot.Message;

@SPI(BotOperation.class)
public class SayOperation extends BotOperation {
    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        if (message.startsWith("say ")) {
            getBot().postMessage(event.getChannel(), event.getUser(), message.substring("say ".length()), event.isTell());
            return true;
        }
        return false;
    }
}