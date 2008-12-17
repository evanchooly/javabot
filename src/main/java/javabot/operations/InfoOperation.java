package javabot.operations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Simple operation to pull who added the factoid and
 * when it was added
 * @author Robert O'Connor <robby DOT oconnor AT gmail.com>
 * @author ricky_clarkson - original code (mostly pulled from LiteralOperation
 */
public class InfoOperation extends BotOperation {
    @Autowired
    private FactoidDao dao;

    public InfoOperation(Javabot bot) {
        super(bot);
    }

    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage().toLowerCase();
        String channel = event.getChannel();
        if (message.startsWith("info ")) {
            String key = message.substring("info ".length());
            if (dao.hasFactoid(key)) {
                SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd-yyyy' at 'K:mm a, z");
                messages.add(new Message(channel, event, key+ " was added by: "+ dao.getFactoid(key).getUserName()
                        +" on "+sdfDate.format(dao.getFactoid(key).getUpdated())+
                        " and has a literal value of: "+ dao.getFactoid(key).getValue()));
                return messages;
            }
            messages.add(new Message(channel, event, "I have no factoid called \"" + key + "\""));
        }
        return messages;
    }

    @Override
    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}