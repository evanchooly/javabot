package javabot.operations;

import java.util.Collections;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.BotEvent;
import javabot.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPI(BotOperation.class)
public class QuitOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(QuitOperation.class);

    @Override
    public List<Message> handleMessage(final BotEvent event) {
        final String message = event.getMessage();
        if(message.toLowerCase().startsWith("quit ")) {
            if(message.substring("quit ".length()).equals(getBot().getNickPassword())) {
                if(log.isDebugEnabled()) {
                    log.debug("About to quit");
                }
                System.exit(0);
            }
        }
        return Collections.emptyList();
    }
}