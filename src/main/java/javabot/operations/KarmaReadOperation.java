package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;
import javabot.dao.KarmaDao;
import javabot.model.Karma;

public class KarmaReadOperation extends BotOperation {
    private KarmaDao karmaDao;

    public KarmaReadOperation(Javabot javabot, KarmaDao dao) {
        super(javabot);
        karmaDao = dao;
    }

    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String sender = event.getSender();
        if(!message.startsWith("karma ")) {
            return messages;
        }
        String nick = message.substring("karma ".length());
        nick = nick.toLowerCase();
        if(nick.contains(" ")) {
            messages.add(new Message(channel, event, "I've never Seen a nick with a space " + "in, " + sender));
            return messages;
        }
        Karma karma = karmaDao.find(nick);
        if(karma != null) {
            if(nick.equals(sender)) {
                messages.add(new Message(channel, event,
                    sender + ", you have a karma level of " + karma.getValue() + "."));
            } else {
                messages.add(new Message(channel, event,
                    nick + " has a karma level of " + karma.getValue() + ", " + sender));
            }
        } else {
            messages.add(new Message(channel, event, nick + " has no karma, " + sender));
        }
        return messages;
    }

    @Override
    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}