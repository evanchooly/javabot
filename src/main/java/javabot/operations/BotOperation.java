package javabot.operations;

import java.util.Collections;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

public abstract class BotOperation {
    private Javabot bot;

    public BotOperation(Javabot javabot) {
        bot = javabot;
    }

    public Javabot getBot() {
        return bot;
    }

    /**
     * Returns a list of BotOperation.Message, empty if the operation was not
     * applicable to the message passed. It should never return null.
     * @param event
     * @return
     */
    public List<Message> handleMessage(BotEvent event) {
        return Collections.emptyList();
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return Collections.emptyList();
    }
}