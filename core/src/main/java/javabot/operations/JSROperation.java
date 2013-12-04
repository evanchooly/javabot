package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.operations.locator.JCPJSRLocator;

@SPI(BotOperation.class)
public class JSROperation extends BotOperation {
    @Inject
    JCPJSRLocator locator;

    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        final String message = event.getMessage().toLowerCase();
        final String channel = event.getChannel();
        final List<Message> responses = new ArrayList<>();
        if ("jsr".equals(message)) {
            responses.add(new Message(channel, event, "Please supply a JSR number to look up."));
        } else {
            if (message.startsWith("jsr ")) {
                final String jsrString = message.substring("jsr ".length());

                try {
                    final int jsr = Integer.parseInt(jsrString);
                    String response = locator.findInformation(jsr);
                    if (response != null && !response.isEmpty()) {
                        responses.add(new Message(channel, event, response));
                    } else {
                        responses.add(new Message(channel, event, "I'm sorry, I can't find a JSR " + jsrString));
                    }
                } catch (NumberFormatException nfe) {
                    responses.add(new Message(channel, event,
                            "'"+jsrString + "' is not a valid JSR reference."));
                }
            }
        }
        return responses;
    }
}

