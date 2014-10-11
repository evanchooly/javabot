package javabot.operations;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.pircbotx.Channel;

import javax.inject.Inject;

@SPI(BotOperation.class)
public class LiteralOperation extends BotOperation {
    @Inject
    private FactoidDao dao;

    /**
     * @see BotOperation#handleMessage(Message)
     */
    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue().toLowerCase();
        final Channel channel = event.getChannel();
        if (message.startsWith("literal ")) {
            final String key = message.substring("literal ".length());
            final Factoid factoid = dao.getFactoid(key);
            if (factoid != null) {
                getBot().postMessage(channel, event.getUser(), factoid.getValue(), event.isTell());
            } else {
                getBot().postMessage(channel, event.getOriginalUser(), Sofia.factoidUnknown(key),
                                     event.isTell() && event.getSender() == null);
            }
        }
        return false;
    }
}
