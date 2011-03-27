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
import javabot.IrcUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SPI(BotOperation.class)
public class KarmaOperation extends BotOperation {
    @Autowired
    private KarmaDao dao;
    private static final Logger log = LoggerFactory.getLogger(KarmaOperation.class);
    private static final Throttler<KarmaInfo> throttler = new Throttler<KarmaInfo>(100, Javabot.THROTTLE_TIME);

    public static final class KarmaInfo implements ThrottleItem<KarmaInfo> {
        private final IrcUser user;
        private final String target;

        public KarmaInfo(final IrcUser user, final String target) {
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
        final List<Message> responses = new ArrayList<Message>();
        responses.addAll(readKarma(event));
        if (responses.isEmpty()) {
            String message = event.getMessage();
            final IrcUser sender = event.getSender();
            final String channel = event.getChannel();
            final String nick;
            try {
                nick = message.substring(0, message.length() - 2).trim().toLowerCase();
            } catch (StringIndexOutOfBoundsException e) {
                throw e;
            }
            if (message.contains(" ") || "".equals(nick)) {
            } else if (!channel.startsWith("#") && (message.endsWith("++") || message.endsWith("--"))) {
                responses.add(new Message(channel, event, "Sorry, karma changes are not allowed in private messages."));
            } else if (message.endsWith("++") || message.endsWith("--")) {
                if (throttler.isThrottled(new KarmaInfo(sender, nick))) {
                    responses.add(new Message(channel, event, "Rest those fingers, Tex"));
                } else {
                    throttler.addThrottleItem(new KarmaInfo(sender, nick));
                    if (nick.equalsIgnoreCase(sender.getNick())) {
                        responses.add(new Message(channel, event, "Changing one's own karma is not permitted."));
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
                    responses.addAll(readKarma(new IrcEvent(event.getChannel(), event.getSender(), "karma " + nick)));
                }
            }
        }
        return responses;
    }

    public List<Message> readKarma(final IrcEvent event) {
        final String message = event.getMessage();
        final String channel = event.getChannel();
        final IrcUser sender = event.getSender();
        final List<Message> response = new ArrayList<Message>();
        if (message.startsWith("karma ")) {
            final String nick = message.substring("karma ".length()).toLowerCase();
            final Karma karma = dao.find(nick);
            if (karma != null) {
                if (nick.equalsIgnoreCase(sender.getNick())) {
                    response.add(new Message(channel, event,
                        sender + ", you have a karma level of " + karma.getValue()));
                } else {
                    response.add(new Message(channel, event,
                        nick + " has a karma level of " + karma.getValue() + ", " + sender));
                }
            } else {
                response.add(new Message(channel, event, nick + " has no karma, " + sender));
            }
        }
        return response;
    }
}