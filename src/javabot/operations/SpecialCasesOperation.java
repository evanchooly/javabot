package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;

import com.rickyclarkson.java.util.TypeSafeList;

/**
 * @author ricky_clarkson
 */
public class SpecialCasesOperation implements BotOperation {
    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);

        String message = event.getMessage();

        String[] stupidPrefixes = { "what is ", "where is " };

        for (int a = 0; a < stupidPrefixes.length; a++) {
            if (message.toLowerCase().startsWith(stupidPrefixes[a]))
                return event.getBot().getResponses(event.getChannel(),
                    event.getSender(), event.getLogin(), event.getHostname(),
                    message.substring(stupidPrefixes[a].length()));
        }

        if (message.toLowerCase().startsWith("no, ")) {
            message = message.substring("no, ".length());

            String key = message.substring(0, message.indexOf(" is "));

            //messages.addAll
            //(
            event.getBot().getResponses(event.getChannel(), event.getSender(),
                event.getLogin(), event.getHostname(), "forget " + key
            //	)
                );

            messages.addAll(event.getBot().getResponses(event.getChannel(),
                event.getSender(), event.getLogin(), event.getHostname(),
                message));

            return messages;
        }

        return messages;
    }

    public List handleChannelMessage(BotEvent event)
    {
	    	return new TypeSafeList(new ArrayList(),Message.class);
    }
}
