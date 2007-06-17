package javabot.operations;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ChangesDao;
import javabot.dao.KarmaDao;
import javabot.dao.model.Karma;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class KarmaChangeOperation implements BotOperation {

    private static Log log = LogFactory.getLog(KarmaChangeOperation.class);
    private final Javabot javabot;

    private ChangesDao c_dao;
    private KarmaDao k_dao;
    private String htmlFile;

    public KarmaChangeOperation(KarmaDao karma_dao, ChangesDao change_dao, final Javabot bot) {
        this.k_dao = karma_dao;
        this.c_dao = change_dao;
        javabot = bot;

    }

    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String sender = event.getSender();
        String channel = event.getChannel();
        if (message.indexOf(" ") != -1) {
            return messages;
        }
        if (message.endsWith("++") || message.endsWith("--")) {
            String nick = message.substring(0, message.length() - 2);
            if (!javabot.isOnSameChannelAs(nick)) {
                messages.add(new Message(channel, "I don't know who that is.", false));
            } else {
                nick = nick.toLowerCase();
                Integer karmavalue = 0;
                boolean newKarma = false;
                Karma karma = new Karma();

                if (k_dao.hasKarma(nick)) {

                    karma = k_dao.getKarma(nick);
                    karmavalue = (karma.getValue());

                }

                if (karmavalue == 0) {
                    newKarma = true;
                    karma = new Karma();

                    karma.setName(nick);
                    karma.setValue(0);
                    karma.setUserName(sender);

                }

                if (nick.equals(sender.toLowerCase())) {
                    messages.add(new Message(channel, "Changing one's own karma is not permitted.", false));
                    message = "--";
                }
                if (message.endsWith("++")) {
                    karmavalue++;
                } else {
                    karmavalue--;
                }
                karma.setValue(karmavalue);
                karma.setUserName(sender);

                if (newKarma) {
                    k_dao.addKarma(karma.getUserName(), karma.getName(), karma.getValue());
                } else {
                    k_dao.updateKarma(karma, c_dao);
                }
                KarmaReadOperation karmaRead = new KarmaReadOperation(k_dao);
                messages.addAll(karmaRead.handleMessage(new BotEvent(event.getChannel(), event.getSender(), event.getLogin(), event.getHostname(), "karma " + nick)));
            }
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}