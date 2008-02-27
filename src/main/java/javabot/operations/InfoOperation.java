package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.FactoidDao;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;


/**
 * Simple operation to pull who added the factoid and
 * when it was added
 * @author Robert O'Connor <robby DOT oconnor AT gmail.com>
 * @author ricky_clarkson - original code (mostly pulled from LiteralOperation
 */
public class InfoOperation implements BotOperation {
    private final FactoidDao dao;

    public InfoOperation(FactoidDao factoidDao) {
        dao = factoidDao;
    }

    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage().toLowerCase();
        String channel = event.getChannel();
        if (message.startsWith("info ")) {
            String key = message.substring("info ".length());
            if (dao.hasFactoid(key)) {
                SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd-yyyy' at 'K:mm a, z");
                messages.add(new Message(channel, key+ " was added by: "+ dao.getFactoid(key).getUserName()
                        +" on "+sdfDate.format(dao.getFactoid(key).getUpdated())+
                        " and has a literal value of: "+ dao.getFactoid(key).getValue(),
                        false));
                return messages;
            }
            messages.add(new Message(channel, "I have no factoid called \"" + key + "\"",
                    false));
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}