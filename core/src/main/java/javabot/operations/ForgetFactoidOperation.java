package javabot.operations;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.pircbotx.Channel;

import javax.inject.Inject;

@SPI(StandardOperation.class)
public class ForgetFactoidOperation extends StandardOperation {
    @Inject
    private FactoidDao factoidDao;

    @Override
    public boolean handleMessage(final Message event) {
        final Channel channel = event.getChannel();
        String message = event.getValue();
        boolean handled = false;
        if (message.startsWith("forget ")) {
            if (!channel.getName().startsWith("#") && !isAdminUser(event.getUser())) {
                getBot().postMessage(channel, event.getUser(), Sofia.privmsgChange(), event.isTell());
            } else {
                message = message.substring("forget ".length());
                if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
                    message = message.substring(0, message.length() - 1);
                }
                handled = forget(event, message.toLowerCase());
            }
        }
        return handled;
    }

    protected boolean forget(final Message event, final String key) {
        final Factoid factoid = factoidDao.getFactoid(key);
        if (factoid != null) {
            if (!factoid.getLocked() || isAdminUser(event.getUser())) {
                getBot().postMessage(event.getChannel(), event.getUser(), Sofia.factoidForgotten(key, event.getUser().getNick()),
                                     event.isTell());
                factoidDao.delete(event.getUser().getNick(), key);
            } else {
                getBot().postMessage(event.getChannel(), event.getUser(), Sofia.factoidDeleteLocked(event.getUser().getNick()),
                                     event.isTell());
            }
        } else {
            getBot().postMessage(event.getChannel(), event.getUser(), Sofia.factoidDeleteUnknown(key, event.getUser().getNick()),
                                 event.isTell());
        }

        return true;
    }
}
