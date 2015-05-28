package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.pircbotx.Channel;

import javax.inject.Inject;

public class LiteralOperation extends BotOperation {
    @Inject
    private FactoidDao dao;

    /**
     * @see BotOperation#handleMessage(Message)
     */
    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue().toLowerCase();
        if (message.startsWith("literal ")) {
            final String key = message.substring("literal ".length());
            final Factoid factoid = dao.getFactoid(key);
            getBot().postMessageToChannel(event, factoid != null ? factoid.getValue() : Sofia.factoidUnknown(key));
            return true;
        }
        return false;
    }
}
