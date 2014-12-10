package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.KarmaDao;
import javabot.model.Karma;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.regex.Pattern;

public class KarmaOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(KarmaOperation.class);

    private static final Pattern optionPattern = Pattern.compile("\\s--\\p{Alpha}[\\p{Alnum}]*=");

    @Inject
    private KarmaDao dao;

    @Override
    public boolean handleMessage(final Message event) {
        boolean handled = readKarma(event);
        if (!handled) {
            String message = event.getValue();
            final User sender = event.getUser();
            final Channel channel = event.getChannel();
            int operationPointer = message.indexOf("++");
            boolean increment = true;
            if (operationPointer == -1) {
                operationPointer = message.indexOf("--");
                increment = false;
                // check for no karma inc/dec, and ~-- and ~++ too
                if (operationPointer < 1) {
                    return false;
                }
            }

            /*
             * we won't get here unless operationPointer>0.
             *
             * But things get wonky; we need to handle two alternatives if it's a
             * karma decrement. One is: "admin --name=foo" and the other is
             * "foo --". We may need to apply a regex to ascertain whether
             * the signal is an option or not.
             *
             * The regex assumes options look like "--foo="
             */
            if (!increment) {
                final String potentialParam = message.substring(operationPointer - 1);
                if (optionPattern.matcher(potentialParam).find()) {
                    // we PRESUMABLY have an option...
                    return false;
                }
            }
            final String nick;
            try {
                nick = message.substring(0, operationPointer).trim().toLowerCase();
            } catch (StringIndexOutOfBoundsException e) {
                log.info("message = " + message, e);
                throw e;
            }
            // got an empty nick; spaces only?
            if (!nick.isEmpty()) {
                if (!channel.getName().startsWith("#")) {
                    getBot().postMessage(channel, event.getUser(), Sofia.privmsgChange(), event.isTell());
                    handled = true;
                } else {
                    if (nick.equalsIgnoreCase(sender.getNick())) {
                        if (increment) {
                            getBot().postMessage(channel, event.getUser(), Sofia.karmaOwnIncrement(), event.isTell());
                        }
                        increment = false;
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
                    handled = readKarma(new Message(event.getChannel(), event.getUser(), "karma " + nick));
                }
            }
        }
        return handled;
    }

    public boolean readKarma(final Message event) {
           final String message = event.getValue();
           final Channel channel = event.getChannel();
           final User sender = event.getUser();
           if (message.startsWith("karma ")) {
               final String nick = message.substring("karma ".length()).toLowerCase();
               final Karma karma = dao.find(nick);
               if (karma != null) {
                   getBot().postMessage(channel, event.getUser(), nick.equalsIgnoreCase(sender.getNick())
                                                                  ? Sofia.karmaOwnValue(sender.getNick(), karma.getValue())
                                                                  : Sofia.karmaOthersValue(nick, karma.getValue(), sender.getNick())
                                           , event.isTell());
               } else {
                   getBot().postMessage(channel, event.getUser(), sender.getNick().equals(nick)
                                                                  ? Sofia.karmaOwnNone(sender.getNick())
                                                                  : Sofia.karmaOthersNone(nick, sender.getNick())
                                           , event.isTell());
               }
               return true;
   
           }
           return false;
       }

}