package javabot.operations;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;

@SPI(BotOperation.class)
public class IgnoreOperation extends BotOperation {
    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        if (message.startsWith("ignore ")) {
            final String[] parts = message.split(" ");
            getBot().addIgnore(parts[1]);
            getBot().postMessage(event.getChannel(), event.getUser(), Sofia.botIgnoring(parts[1]), event.isTell());
            return true;
        }
        return false;
    }
}
