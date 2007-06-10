package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.KarmaDao;

import java.util.ArrayList;
import java.util.List;

public class KarmaReadOperation implements BotOperation {

    private KarmaDao k_dao;

    public KarmaReadOperation(KarmaDao karma_dao) {
        this.k_dao = karma_dao;
    }

    /**
     * @see javabot.operations.BotOperation#handleMessage(javabot.BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        String sender = event.getSender();
        if (!message.startsWith("karma ")) {
            return messages;
        }
        String nick = message.substring("karma ".length());
        nick = nick.toLowerCase();
        if (nick.indexOf(" ") != -1) {
            messages.add(new Message(channel, "I've never Seen a nick with a space " + "in, " + sender, false));
            return messages;
        }
        Integer karma = 0;

        if (k_dao.hasKarma(nick)) {
            karma = k_dao.getKarma(nick).getValue();
        }

        if (nick.equals(sender)) {
            messages.add(new Message(channel, sender + ", you have a karma level of " + karma + ".", false));
        } else {
            messages.add(new Message(channel, nick + " has a karma level of " + karma + ", " + sender, false));
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}