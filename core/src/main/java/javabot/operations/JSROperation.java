package javabot.operations;

import java.util.List;
import java.util.ArrayList;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.operations.locator.JCPJSRLocator;
import javabot.operations.locator.impl.JCPJSRLocatorImpl;

@SPI(BotOperation.class)
public class JSROperation extends BotOperation {
    JCPJSRLocator locator = new JCPJSRLocatorImpl();

    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        final String message = event.getMessage().toLowerCase();
        final String channel = event.getChannel();
        final List<Message> responses = new ArrayList<Message>();
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
