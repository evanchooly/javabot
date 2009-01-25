package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.operations.throttle.Throttler;
import javabot.operations.throttle.ThrottleItem;
import javabot.dao.KarmaDao;
import javabot.model.Karma;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class KarmaChangeOperation extends BotOperation {
    @Autowired
    private KarmaDao dao;
    private static final Logger log = LoggerFactory.getLogger(KarmaChangeOperation.class);
    private static final Throttler<KarmaInfo> throttler = new Throttler<KarmaInfo>(100, 20 * 1000);

    public KarmaChangeOperation(Javabot bot) {
        super(bot);
    }

    public static final class KarmaInfo implements ThrottleItem<KarmaInfo> {
        private final String nick;
        private final String target;

        public KarmaInfo(String nick, String target) {
            this.nick = nick;
            this.target = target;
        }

        public boolean matches(KarmaInfo ki) {
            return nick.equals(ki.nick) && target.equals(ki.target);
        }
    }

    @Override
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
        if (!channel.startsWith("#") && (message.endsWith("++") || message.endsWith("--"))) {
            messages.add(new Message(channel, event, "Sorry, karma changes are not allowed in private messages."));
            return messages;
        }
        if (message.endsWith("++") || message.endsWith("--")) {
            if (throttler.isThrottled (new KarmaInfo(sender, nick))) {
                if (log.isDebugEnabled()) {
                    log.debug("skipping karma change by " + nick + "for " + nick);
                }
                messages.add(new Message(channel, event, "Rest those fingers, Tex"));
                return messages;
            }
	    throttler.addThrottleItem (new KarmaInfo(sender, nick));
            if (nick.equals(sender.toLowerCase())) {
                messages.add(new Message(channel, event, "Changing one's own karma is not permitted."));
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
            KarmaReadOperation karmaRead = (KarmaReadOperation) getBot()
                .getOperation(KarmaReadOperation.class.getName());
            messages.addAll(karmaRead.handleMessage(new BotEvent(event.getChannel(), event.getSender(),
                event.getLogin(), event.getHostname(), "karma " + nick)));
        }
        return messages;
    }
}
