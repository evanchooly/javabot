package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.KarmaDao;
import javabot.model.Karma;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KarmaChangeOperation implements BotOperation {
    private KarmaDao dao;
    private static final Logger log = LoggerFactory.getLogger(KarmaChangeOperation.class);
    private static final int MAX_THROTTLE_MEM = 100;
    private static final int THROTTLE_TIME = 20 * 1000; // 20 seconds.
    private final List<KarmaInfo> lastKarmaChange = new ArrayList<KarmaInfo>(MAX_THROTTLE_MEM);

    public KarmaChangeOperation(KarmaDao karmaDao) {
        dao = karmaDao;
    }

    // most of this code is ripped from ernimril's
    // throttling patch for TellOperation.
    public static final class KarmaInfo {
        private final long when;
        private final String nick;
        private final String target;

        public KarmaInfo(String nick,String target) {
            this.nick = nick;
            this.target = target;
            when = System.currentTimeMillis();
        }

        public long getWhen() {
            return when;
        }

        public boolean match(String nick, String target) {
            return this.nick.equals(nick) && this.target.equals(target);
        }
    }

    @Transactional
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String sender = event.getSender();
        String channel = event.getChannel();
        String nick = message.substring(0, message.length() - 2).trim().toLowerCase();
        if (message.contains(" ") || "".equals(nick)) {
            return messages;
        }
        if(!channel.startsWith("#") && (message.endsWith("++") || message.endsWith("--"))) {
            messages.add(new Message(channel, "Sorry, karma changes are not allowed in private messages.", false));
            return messages;
        }
        if(message.endsWith("++") || message.endsWith("--")) {
            if(alreadyChanged(sender,nick)) {
                if(log.isDebugEnabled()) {
                    log.debug("skipping karma change by "+nick+"for "+nick);
                }
                messages.add(new Message(channel,"Rest those fingers, Tex",false));
                return messages;
            }
            addKarmaChanged(sender,nick);
            if(nick.equals(sender.toLowerCase())) {
                messages.add(new Message(channel, "Changing one's own karma is not permitted.", false));
                message = "--";
            }
            Karma karma = dao.find(nick);
            if (karma == null) {
                karma = new Karma();
                karma.setName(nick);
            }
            if (message.endsWith("++")) {
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

    private boolean alreadyChanged(String nick, String target) {
        long now = System.currentTimeMillis();
        if(log.isDebugEnabled()) {
            log.debug("already Changed: " + target + "'s" + "by "+nick );
        }
        for(KarmaInfo ki : lastKarmaChange) {
            if(log.isDebugEnabled()) {
                log.debug(ki.nick + "changed the karma of " + ki.target);
            }
            if(now - ki.getWhen() < THROTTLE_TIME && ki.match(nick, target))
                return true;
        }
        return false;

    }

    private void addKarmaChanged(String nick, String target) {
        KarmaInfo ki = new KarmaInfo(nick,target);
        lastKarmaChange.add(ki);
        if(lastKarmaChange.size() > MAX_THROTTLE_MEM)
            lastKarmaChange.remove(0);
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
