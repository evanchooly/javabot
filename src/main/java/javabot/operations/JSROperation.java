package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.operations.locator.JCPJSRLocator;
import javabot.operations.locator.impl.JCPJSRLocatorImpl;

public class JSROperation implements BotOperation {
    JCPJSRLocator locator=new JCPJSRLocatorImpl();
    
    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage().toLowerCase();
        String channel = event.getChannel();
        if(message.startsWith("jsr ")) {
            String jsrString = message.substring("jsr ".length());
            try {
                int jsr=Integer.parseInt(jsrString);
                messages.add(new Message(channel, locator.findInformation(jsr), false));
            } catch(NumberFormatException nfe) {
            }

        }
        return messages;
    }

    @Override
    public List<Message> handleChannelMessage(BotEvent event) {
        return handleMessage(event);
    }
}
