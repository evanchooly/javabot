package javabot.operations;

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
    public boolean handleMessage(final BotEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final String sender = event.getSender();
        boolean handled = false;
        if (message.startsWith("karma ")) {
            final String nick = message.substring("karma ".length()).toLowerCase();
            final Karma karma = karmaDao.find(nick);
            if (karma != null) {
                if (nick.equalsIgnoreCase(sender)) {
                    getBot().postMessage(new Message(channel, event,
                        sender + ", you have a karma level of " + karma.getValue()));
                } else {
                    getBot().postMessage(new Message(channel, event,
                        nick + " has a karma level of " + karma.getValue() + ", " + sender));
                }
            } else {
                getBot().postMessage(new Message(channel, event, nick + " has no karma, " + sender));
            }
            handled = true;
        }
        return handled;
    }
}