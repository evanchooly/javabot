package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.KarmaDao;
import javabot.model.Karma;
import javabot.operations.throttle.ThrottleItem;
import javabot.operations.throttle.Throttler;
import org.schwering.irc.lib.IRCUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SPI(BotOperation.class)
public class KarmaChangeOperation extends BotOperation {
    @Autowired
    private KarmaDao dao;
    private static final Logger log = LoggerFactory.getLogger(KarmaChangeOperation.class);
    private static final Throttler<KarmaInfo> throttler = new Throttler<KarmaInfo>(100, Javabot.THROTTLE_TIME);
    private KarmaReadOperation karmaRead;

    public KarmaChangeOperation() {
    }

    public static final class KarmaInfo implements ThrottleItem<KarmaInfo> {
        private final IRCUser user;
        private final String target;

        public KarmaInfo(final IRCUser user, final String target) {
            this.user = user;
            this.target = target;
        }

        public boolean matches(final KarmaInfo ki) {
            return user.equals(ki.user) && target.equals(ki.target);
        }
    }

    @Override
    @Transactional
    public List<Message> handleMessage(final IrcEvent event) {
        if (karmaRead == null) {
            karmaRead = (KarmaReadOperation) getBot().getOperation("KarmaRead");
        }
        String message = event.getMessage();
        final IRCUser sender = event.getSender();
        final String channel = event.getChannel();
        final String nick;
        try {
            nick = message.substring(0, message.length() - 2).trim().toLowerCase();
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("KarmaChangeOperation.handleMessage: message = " + message);
            throw e;
        }
        final List<Message> response = new ArrayList<Message>();
        if (message.contains(" ") || "".equals(nick)) {
        } else if (!channel.startsWith("#") && (message.endsWith("++") || message.endsWith("--"))) {
            response.add(new Message(channel, event, "Sorry, karma changes are not allowed in private messages."));
        } else if (message.endsWith("++") || message.endsWith("--")) {
            if (throttler.isThrottled(new KarmaInfo(sender, nick))) {
                if (log.isDebugEnabled()) {
                    log.debug("skipping karma change by " + nick + " for " + nick);
                }
                response.add(new Message(channel, event, "Rest those fingers, Tex"));
            } else {
                throttler.addThrottleItem(new KarmaInfo(sender, nick));
                if (nick.equalsIgnoreCase(sender.getNick())) {
                    response.add(new Message(channel, event, "Changing one's own karma is not permitted."));
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
                karma.setUserName(sender.getNick());
                dao.save(karma);
                response.addAll(karmaRead.handleMessage(new IrcEvent(event.getChannel(), event.getSender(),
                    "karma " + nick)));
            }
        }
        return response;
    }
}