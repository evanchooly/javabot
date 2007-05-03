package javabot.operations;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ChangesDao;
import javabot.dao.FactoidDao;
import javabot.dao.model.factoids;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class KarmaChangeOperation2 implements BotOperation {

    private static Log log = LogFactory.getLog(KarmaChangeOperation2.class);
    private final Javabot javabot;

    private FactoidDao f_dao;
    private ChangesDao c_dao;
    private String htmlFile;

    public KarmaChangeOperation2(FactoidDao factoid_dao, ChangesDao change_dao, String file, final Javabot bot) {
        this.f_dao = factoid_dao;
        this.c_dao = change_dao;
        javabot = bot;
        htmlFile = file;
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
                int karma = 0;
                boolean newKarma = false;
                factoids factoid = f_dao.getFactoid("karma " + nick);
                try {
                    karma = Integer.parseInt(factoid.getValue());
                } catch (Exception exception) {
                    newKarma = true;
                    factoid = new factoids();

                    factoid.setName("karma " + nick);
                    factoid.setValue("0");
                    factoid.setUserName(sender);

                }
                if (nick.equals(sender.toLowerCase())) {
                    messages.add(new Message(channel, "Changing one's own karma is not permitted.", false));
                    message = "--";
                }
                if (message.endsWith("++")) {
                    karma++;
                } else {
                    karma--;
                }
                factoid.setValue("" + karma);
                factoid.setUserName(sender);

                if (newKarma) {
                    f_dao.addFactoid(factoid.getUserName(), factoid.getName(), factoid.getValue(), c_dao, htmlFile);
                } else {
                    f_dao.updateFactoid(factoid, c_dao, htmlFile);
                }
                KarmaReadOperation2 karmaRead = new KarmaReadOperation2(f_dao, c_dao, htmlFile);
                messages.addAll(karmaRead.handleMessage(new BotEvent(event.getChannel(), event.getSender(), event.getLogin(), event.getHostname(), "karma " + nick)));
            }
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}