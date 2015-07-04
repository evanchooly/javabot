package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;

public class IgnoreOperation extends BotOperation {
    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        if (message.startsWith("ignore ")) {
            final String[] parts = message.split(" ");
            getBot().addIgnore(parts[1]);
            getBot().postMessageToChannel(event, Sofia.botIgnoring(parts[1]));
            return true;
        }
        return false;
    }
}
