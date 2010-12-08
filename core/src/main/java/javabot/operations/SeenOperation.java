package javabot.operations;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.dao.LogsDao;
import org.schwering.irc.lib.IRCUser;
import org.springframework.beans.factory.annotation.Autowired;

@SPI(BotOperation.class)
public class SeenOperation extends BotOperation {
    @Autowired
    private LogsDao dao;

    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final IRCUser sender = event.getSender();
        final List<Message> responses = new ArrayList<Message>();
        if ("seen ".equalsIgnoreCase(message.substring(0, Math.min(message.length(), 5)))) {
            final String key = message.substring("seen ".length());
            if (dao.isSeen(key, channel)) {
                responses.add(new Message(channel, event,
                    String.format("%s, %s was last seen at %s with the following entry: %s", sender, key,
                        DateFormat.getInstance().format(dao.getSeen(key, channel).getUpdated()),
                        dao.getSeen(key, channel).getMessage())));
            } else {
                responses.add(new Message(channel, event,
                    String.format("%s, I have no information about \"%s\"", sender, key)));
            }
        }
        return responses;
    }
}