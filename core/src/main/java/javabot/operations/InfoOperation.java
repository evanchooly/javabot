package javabot.operations;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.pircbotx.Channel;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple operation to pull who added the factoid and when it was added
 */
@SPI(BotOperation.class)
public class InfoOperation extends BotOperation {
    public static final String INFO_DATE_FORMAT = "dd MMM yyyy' at 'KK:mm";

    @Inject
    private FactoidDao dao;

    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue().toLowerCase();
        final Channel channel = event.getChannel();
        if (message.startsWith("info ")) {
            final String key = message.substring("info ".length());
            final Factoid factoid = dao.getFactoid(key);
            if (factoid != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(INFO_DATE_FORMAT);
                LocalDateTime updated = factoid.getUpdated();
                String formatted = formatter.format(updated);
                getBot().postMessage(channel, event.getUser(),
                                     Sofia.factoidInfo(key, factoid.getLocked() ? "*" : "", factoid.getUserName(),
                                                       formatted, factoid.getValue()),
                                     event.isTell());
            } else {
                getBot().postMessage(channel, event.getOriginalUser(), Sofia.factoidUnknown(key),
                                     event.isTell() && event.getSender() == null);
            }
            return true;
        }
        return false;
    }

}