package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.Responder;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ricky_clarkson
 */
public class SpecialCasesOperation implements BotOperation {
    private static final Logger log = LoggerFactory.getLogger(SpecialCasesOperation.class);
    private final Responder responder;

    public SpecialCasesOperation(Responder resp) {
        responder = resp;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
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
            responder.getResponses(event.getChannel(), event.getSender(),
                    event.getLogin(), event.getHostname(), "forget " + key
            );
            messages.addAll(responder.getResponses(event.getChannel(),
                    event.getSender(), event.getLogin(), event.getHostname(),
                    message));
            return messages;
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
