package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.IrcUser;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.KarmaDao;
import javabot.model.Karma;
import javabot.operations.throttle.ThrottleItem;
import javabot.operations.throttle.Throttler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPI(BotOperation.class)
public class KarmaOperation extends BotOperation {
    @SuppressWarnings("UnusedDeclaration")
    private static final Logger log = LoggerFactory.getLogger(KarmaOperation.class);
    private static final Throttler<KarmaInfo> throttler = new Throttler<>(100, Javabot.THROTTLE_TIME);
    @Inject
    private KarmaDao dao;

    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        final List<Message> responses = new ArrayList<>();
        responses.addAll(readKarma(event));
        if (responses.isEmpty()) {
            String message = event.getMessage();
            final IrcUser sender = event.getSender();
            final String channel = event.getChannel();
            boolean increment = message.endsWith("++");
            boolean decrement = !increment && message.endsWith("--");
            if (!(increment || decrement) || message.length() <= 2) {
                return responses;
            }
            final String nick;
            try {
                nick = message.substring(0, message.length() - 2).trim().toLowerCase();
            } catch (StringIndexOutOfBoundsException e) {
                log.info("message = " + message, e);
                return responses;
            }
            if (!channel.startsWith("#")) {
                responses.add(new Message(channel, event, "Sorry, karma changes are not allowed in private messages."));
            }
            if (responses.isEmpty()) {
                if (throttler.isThrottled(new KarmaInfo(sender, nick))) {
                    responses.add(new Message(channel, event, "Rest those fingers, Tex"));
                } else {
                    throttler.addThrottleItem(new KarmaInfo(sender, nick));
                    if (nick.equalsIgnoreCase(sender.getNick())) {
                        if (increment) {
                            responses.add(new Message(channel, event, "You can't increment your own karma."));
                        }
                        increment = false;
                        decrement = true;
                    }
                    Karma karma = dao.find(nick);
                    if (karma == null) {
                        karma = new Karma();
                        karma.setName(nick);
                    }
                    if (increment) {
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
        final List<Message> response = new ArrayList<>();
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
}