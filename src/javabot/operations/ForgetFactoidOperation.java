package javabot.operations;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import com.rickyclarkson.java.util.Arrays;
import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;

/**
 * @author ricky_clarkson
 */
public class ForgetFactoidOperation implements BotOperation {
    /**
     * @see javabot.operations.BotOperation#handleMessage(javabot.BotEvent)
     */
    public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);
        String channel = event.getChannel();
        String message = event.getMessage();
        String sender = event.getSender();
        Javabot bot = event.getBot();
        String[] messageParts = message.split(" ");
        if(messageParts[0].equals("forget")) {
            if(!bot.isValidSender(sender)) {
                int length = Array.getLength(messageParts);
                Object keyParts = Arrays.subset(messageParts, 1, length);
                String key = Arrays.toString(keyParts, " ");
                key = key.toLowerCase();
                if(bot.hasFactoid(key)) {
                    messages.add(new Message(channel, "I forgot about " + key
                        + ", " + sender + ".", false));
                    bot.forgetFactoid(sender, key);
                } else {
                    messages.add(new Message(channel, "I never knew about "
                        + key + " anyway, " + sender + ".", false));
                }
            } else {
                messages.add(new Message(channel, "Whatever, " + sender + ".",
                    false));
            }
        }
        return messages;
    }
}