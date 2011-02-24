package javabot.operations;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.model.Factoid;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Simple operation to pull who added the factoid and when it was added
 */
@SPI(BotOperation.class)
public class InfoOperation extends BotOperation {
    @Autowired
    private FactoidDao dao;

    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        final String message = event.getMessage().toLowerCase();
        final String channel = event.getChannel();
        final List<Message> responses = new ArrayList<Message>();
        if (message.startsWith("info ")) {
            final String key = message.substring("info ".length());
            final Factoid factoid = dao.getFactoid(key);
            if (factoid != null) {
                responses.add(new Message(channel, event,
                    String.format("%s%s was added by: %s on %s and has a literal value of: %s", key,
                        factoid.getLocked() ? "*" : "", factoid.getUserName(), formatDate(factoid), factoid.getValue())));
            } else {
                responses.add(new Message(channel, event, "I have no factoid called \"" + key + "\""));
            }
        }
        return responses;
    }

    private String formatDate(final Factoid factoid) {
        return new SimpleDateFormat("MM-dd-yyyy' at 'K:mm a, z").format(factoid.getUpdated());
    }
}