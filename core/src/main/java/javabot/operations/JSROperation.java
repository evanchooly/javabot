package javabot.operations;

import java.util.List;
import java.util.ArrayList;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.operations.locator.JCPJSRLocator;
import javabot.operations.locator.impl.JCPJSRLocatorImpl;

public class JSROperation extends BotOperation {
    JCPJSRLocator locator = new JCPJSRLocatorImpl();

    public JSROperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public List<Message> handleMessage(final BotEvent event) {
        final String message = event.getMessage().toLowerCase();
        final String channel = event.getChannel();
        List<Message> responses = new ArrayList<Message>();
        if (message.startsWith("jsr ")) {
            final String jsrString = message.substring("jsr ".length());
            try {
                final int jsr = Integer.parseInt(jsrString);
                responses.add(new Message(channel, event, locator.findInformation(jsr)));
            } catch (NumberFormatException nfe) {
                responses.add(new Message(channel, event, jsrString + " is not a number."));
            }
        }
        return responses;
    }
}
