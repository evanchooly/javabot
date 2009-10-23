package javabot.operations;

import java.util.List;
import java.util.ArrayList;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.KarmaDao;
import javabot.model.Karma;
import org.springframework.beans.factory.annotation.Autowired;

public class KarmaReadOperation extends BotOperation {
    @Autowired
    private KarmaDao karmaDao;

    public KarmaReadOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public List<Message> handleMessage(final BotEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final String sender = event.getSender();
        List<Message> response = new ArrayList<Message>();
        if (message.startsWith("karma ")) {
            final String nick = message.substring("karma ".length()).toLowerCase();
            final Karma karma = karmaDao.find(nick);
            if (karma != null) {
                if (nick.equalsIgnoreCase(sender)) {
                    response.add((new Message(channel, event,
                        sender + ", you have a karma level of " + karma.getValue())));
                } else {
                    response.add((new Message(channel, event,
                        nick + " has a karma level of " + karma.getValue() + ", " + sender)));
                }
            } else {
                response.add((new Message(channel, event, nick + " has no karma, " + sender)));
            }
        }
        return response;
    }
}