package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import com.rickyclarkson.java.util.Arrays;
import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Message;

/**
 * @author ricky_clarkson
 */
public class NickometerOperation implements BotOperation {
    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);
        String message = event.getMessage();
        Object[] messageParts = message.split(" ");
        messageParts = Arrays.removeAll(messageParts, "");
        if(messageParts == null
            || !messageParts[0].equals("nickometer")
            || messageParts.length < 2) {
            return messages;
        }
        String nick = messageParts[1].toString();
        if(nick.length() == 0) {
            return messages;
        }
        int lameness = 0;
        if(nick.length() > 0 && Character.isUpperCase(nick.charAt(0))) {
            lameness--;
        }
        for(int a = 0; a < nick.length(); a++) {
            if(!Character.isLowerCase(nick.charAt(a))) {
                lameness++;
            }
        }
        double tempLameness = (double)lameness / nick.length();
        tempLameness = Math.sqrt(tempLameness);
        lameness = (int)(tempLameness * 100);
        messages.add(new Message(event.getChannel(), "The nick " + nick
            + " is " + lameness + "% lame.", false));
        return messages;
    }

    public List handleChannelMessage(BotEvent event)
    {
	    	return new TypeSafeList(new ArrayList(),Message.class);
    }
}
