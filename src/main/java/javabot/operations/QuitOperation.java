package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QuitOperation implements BotOperation {
    private static final Log log = LogFactory.getLog(QuitOperation.class);
    private final String password;

    public QuitOperation(String value) {
        password = value;
    }

    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        if(message.toLowerCase().startsWith("quit ")) {
            if(message.substring("quit ".length()).equals(password)) {
                log.debug("About to quit");
                System.exit(0);
            }
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}