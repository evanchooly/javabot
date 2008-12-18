package javabot.operations;

import java.util.Collections;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class BotOperation {
    @Autowired
    private ApplicationContext context;
    private Javabot bot;

    public BotOperation(Javabot javabot) {
        bot = javabot;
    }

    public Javabot getBot() {
        return bot;
    }

    public ApplicationContext getContext() {
        return context;
    }

    protected void inject(Object object) {
        getContext().getAutowireCapableBeanFactory().autowireBean(object);
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