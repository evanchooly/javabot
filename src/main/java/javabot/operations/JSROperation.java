package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;
import javabot.operations.locator.JCPJSRLocator;
import javabot.operations.locator.impl.JCPJSRLocatorImpl;

public class JSROperation extends BotOperation {
    JCPJSRLocator locator=new JCPJSRLocatorImpl();

    public JSROperation(Javabot javabot) {
        super(javabot);
    }

    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage().toLowerCase();
        String channel = event.getChannel();
        if(message.startsWith("jsr ")) {
            String jsrString = message.substring("jsr ".length());
            try {
                int jsr=Integer.parseInt(jsrString);
                messages.add(new Message(channel, event, locator.findInformation(jsr)));
            } catch(NumberFormatException nfe) {
            }

        }
        return messages;
    }
}
