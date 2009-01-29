package javabot.operations;

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
    public boolean handleMessage(final BotEvent event) {
        final String message = event.getMessage().toLowerCase();
        final String channel = event.getChannel();
        boolean handled = false;
        if (message.startsWith("jsr ")) {
            final String jsrString = message.substring("jsr ".length());
            try {
                final int jsr = Integer.parseInt(jsrString);
                getBot().postMessage(new Message(channel, event, locator.findInformation(jsr)));
            } catch (NumberFormatException nfe) {
                getBot().postMessage(new Message(channel, event, jsrString + " is not a number."));
            }
            handled = true;
        }
        return handled;
    }
}
