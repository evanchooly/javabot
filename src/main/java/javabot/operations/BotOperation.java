package javabot.operations;

import java.util.List;
import javabot.BotEvent;
import javabot.Message;

/**
 * @author ricky_clarkson
 */
public interface BotOperation {
    /**
     * Returns a list of BotOperation.Message, empty if the operation was not
     * applicable to the message passed. It should never return null.
     * @param event
     * @return
     */
    List<Message> handleMessage(BotEvent event);

    List<Message> handleChannelMessage(BotEvent event);
}
