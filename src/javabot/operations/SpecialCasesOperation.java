package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Responder;

import com.rickyclarkson.java.util.TypeSafeList;

/**
 * @author ricky_clarkson
 */
public class SpecialCasesOperation implements BotOperation {
	private final Responder responder;
	
	public SpecialCasesOperation(final Responder responder)
	{
		this.responder=responder;
	}

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);

        String message = event.getMessage();

        if (message.toLowerCase().startsWith("no, ")) {
            message = message.substring("no, ".length());

            String key = message.substring(0, message.indexOf(" is "));

            responder.getResponses(event.getChannel(), event.getSender(),
                event.getLogin(), event.getHostname(), "forget " + key
                );

            messages.addAll(responder.getResponses(event.getChannel(),
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
