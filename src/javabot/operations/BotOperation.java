package javabot.operations;

import java.util.List;
import javabot.BotEvent;

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
    public List handleMessage(BotEvent event);

    public List handleChannelMessage(BotEvent event);
}
