package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ricky_clarkson
 */
public class SpecialCasesOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(SpecialCasesOperation.class);

    public SpecialCasesOperation(Javabot javabot) {
        super(javabot);
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        if(log.isDebugEnabled()) {
            log.debug("SpecialCasesOperation: " + message);
        }

        String lowerMessage = message.toLowerCase();
        if (lowerMessage.startsWith("no")) {
            message = message.substring("no".length());

            if (message.startsWith(",")) {
                message = message.substring(",".length());
            }

            message = message.replaceAll("^\\s+", "");
            if(log.isDebugEnabled()) {
                log.debug("SpecialCasesOperation: " + message);
            }

            String key = message.substring(0, message.indexOf(" is "));
            key = key.replaceAll("^\\s+", "");
            if(log.isDebugEnabled()) {
                log.debug("SpecialCasesOperation: Key " + key);
            }
            getBot().getResponses(event.getChannel(), event.getSender(),
                    event.getLogin(), event.getHostname(), "forget " + key
            );
            messages.addAll(getBot().getResponses(event.getChannel(),
                    event.getSender(), event.getLogin(), event.getHostname(),
                    message));
            return messages;
        }
        return messages;
    }
}
