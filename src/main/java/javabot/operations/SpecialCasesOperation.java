package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.Responder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ricky_clarkson
 */
public class SpecialCasesOperation implements BotOperation {

    private static final Log log = LogFactory.getLog(SpecialCasesOperation.class);
    private final Responder responder;

    public SpecialCasesOperation(final Responder responder) {
        this.responder = responder;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();

        log.debug("SpecialCasesOperation: " + message);

        String lowerMessage = message.toLowerCase();
        if (lowerMessage.startsWith("no")) {
            message = message.substring("no".length());

            if (message.startsWith(",")) {
                message = message.substring(",".length());
            }

            message = message.replaceAll("^\\s+", "");

            log.debug("SpecialCasesOperation: " + message);

            String key = message.substring(0, message.indexOf(" is "));
            key = key.replaceAll("^\\s+", "");

            log.debug("SpecialCasesOperation: Key " + key);
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
