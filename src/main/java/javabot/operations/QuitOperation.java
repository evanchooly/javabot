package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuitOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(QuitOperation.class);

    public QuitOperation(Javabot javabot) {
        super(javabot);
    }

    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        if(message.toLowerCase().startsWith("quit ")) {
            if(message.substring("quit ".length()).equals(getBot().getNickPassword())) {
                if(log.isDebugEnabled()) {
                    log.debug("About to quit");
                }
                System.exit(0);
            }
        }
        return messages;
    }
}