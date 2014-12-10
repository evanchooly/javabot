package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashSet;
import java.util.Set;

public class GetFactoidOperation extends BotOperation implements StandardOperation  {
    @Inject
    private FactoidDao factoidDao;

    @Inject
    private Provider<PircBotX> ircBot;

    @Override
    public boolean handleMessage(final Message event) {
        return tell(event) || getFactoid(null, event, new HashSet<>());
    }

    private boolean getFactoid(final TellSubject subject, final Message event, final Set<String> backtrack) {
        String message = event.getValue();
        if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
            message = message.substring(0, message.length() - 1);
        }
        final String firstWord = message.split(" ")[0];
        final String params = message.substring(firstWord.length()).trim();
        Factoid factoid = factoidDao.getFactoid(message.toLowerCase());
        if (factoid == null) {
            factoid = factoidDao.getParameterizedFactoid(firstWord);
        }

        return factoid != null && getResponse(subject, event, backtrack, params, factoid);
    }

    private boolean getResponse(final TellSubject subject, final Message event, final Set<String> backtrack,
                                final String replacedValue, final Factoid factoid) {
        String sender = event.getUser().getNick();
        final String message = factoid.evaluate(subject, sender, replacedValue);
        if (message.startsWith("<see>")) {
            if (backtrack.contains(message)) {
                getBot().postMessage(event.getChannel(), null, Sofia.factoidLoop(message), event.isTell());
                return true;
            } else {
                backtrack.add(message);
                return getFactoid(subject, new Message(event, message.substring(5).trim()), backtrack);
            }
        } else if (message.startsWith("<reply>")) {
            getBot().postMessage(event.getChannel(), event.getUser(), message.substring("<reply>".length()),
                                 event.isTell() && !message.contains(event.getUser().getNick()));
            return true;
        } else if (message.startsWith("<action>")) {
            getBot().postAction(event.getChannel(), message.substring("<action>".length()));
            return true;
        } else {
            getBot().postMessage(event.getChannel(), event.getUser(), message, event.isTell());
            return true;
        }
    }

    private boolean tell(final Message event) {
        final String message = event.getValue();
        final Channel channel = event.getChannel();
        final User sender = event.getUser();
        boolean handled = false;
        if (isTellCommand(message)) {
            final TellSubject tellSubject = parseTellSubject(event);
            if (tellSubject == null) {
                getBot().postMessage(event.getChannel(), event.getUser(), Sofia.factoidTellSyntax(sender.getNick()), event.isTell());
                handled = true;
            } else {
                User targetUser = tellSubject.getTarget();
                if (targetUser != null) {
                    if ("me".equalsIgnoreCase(targetUser.getNick())) {
                        targetUser = sender;
                    }
                    final String thing = tellSubject.getSubject();
                    if (targetUser.getNick().equalsIgnoreCase(getBot().getNick())) {
                        getBot().postMessage(event.getChannel(), event.getUser(), Sofia.botSelfTalk(), true);
                        handled = true;
                    } else {
                        if (!getBot().isOnCommonChannel(targetUser)) {
                            getBot().postMessage(event.getChannel(), event.getUser(),
                                                 Sofia.userNotInChannel(targetUser.getNick(), channel.getName()), true);
                            handled = true;
                        } else if (sender.getNick().equals(channel.getName()) && !getBot().isOnCommonChannel(targetUser)) {
                            getBot().postMessage(event.getChannel(), event.getUser(), Sofia.userNoSharedChannels(), true);
                            handled = true;
                        } else if (thing.endsWith("++") || thing.endsWith("--")) {
                            getBot().postMessage(event.getChannel(), event.getUser(), Sofia.notAllowed(), true);
                            handled = true;
                        } else {
                            handled = getBot().getResponses(new Message(channel, targetUser, thing, sender), event.getUser());
                        }
                    }
                }
            }
        }
        return handled;
    }

    private TellSubject parseTellSubject(final Message event) {
        String message = event.getValue();
        if (message.startsWith("tell ")) {
            return parseLonghand(event);
        }
        return parseShorthand(event);
    }

    private TellSubject parseLonghand(final Message event) {
        String message = event.getValue();
        final String body = message.substring("tell ".length());
        final String nick = body.substring(0, body.indexOf(" "));
        final int about = body.indexOf("about ");
        if (about < 0) {
            return null;
        }
        final String thing = body.substring(about + "about ".length());
        return new TellSubject(ircBot.get().getUserChannelDao().getUser(nick), thing);
    }

    private TellSubject parseShorthand(final Message event) {
        String target = event.getValue();
        for (final String start : getBot().getStartStrings()) {
            if (target.startsWith(start)) {
                target = target.substring(start.length()).trim();
            }
        }
        final int space = target.indexOf(' ');
        TellSubject tellSubject = null;
        if (space >= 0) {
            User user = ircBot.get().getUserChannelDao().getUser(target.substring(0, space));
            tellSubject = new TellSubject(user, target.substring(space + 1).trim());
        }
        return tellSubject;
    }

    private boolean isTellCommand(final String message) {
        return message.startsWith("tell ") || message.startsWith("~");
    }
}