package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.KarmaDao;
import javabot.model.Karma;
import org.springframework.transaction.annotation.Transactional;

public class KarmaChangeOperation implements BotOperation {
    private KarmaDao dao;

    public KarmaChangeOperation(KarmaDao karmaDao) {
        dao = karmaDao;
    }

    @Transactional
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String sender = event.getSender();
        String channel = event.getChannel();
        if(message.indexOf(" ") != -1) {
            return messages;
        }
        if(message.endsWith("++") || message.endsWith("--")) {
            String nick = message.substring(0, message.length() - 2);
            nick = nick.toLowerCase();
            if(nick.equals(sender.toLowerCase())) {
                messages.add(new Message(channel, "Changing one's own karma is not permitted.", false));
                message = "--";
            }
            Karma karma = dao.find(nick);
            if(karma == null) {
                karma = new Karma();
                karma.setName(nick);
            }
            if(message.endsWith("++")) {
                karma.setValue(karma.getValue() + 1);
            } else {
                karma.setValue(karma.getValue() - 1);
            }
            karma.setUserName(sender);
            dao.save(karma);
            KarmaReadOperation karmaRead = new KarmaReadOperation(dao);
            messages.addAll(karmaRead.handleMessage(new BotEvent(event.getChannel(), event.getSender(),
                event.getLogin(), event.getHostname(), "karma " + nick)));
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}