package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.Seen;
import javabot.dao.LogsDao;
import org.pircbotx.Channel;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;

public class SeenOperation extends BotOperation {
    @Inject
    private LogsDao dao;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        final Channel channel = event.getChannel();
        if (channel != null && "seen ".equalsIgnoreCase(message.substring(0, Math.min(message.length(), 5)))) {
            final String key = message.substring("seen ".length());
            Seen seen = dao.getSeen(channel.getName(), key);
            if (seen != null) {
                getBot().postMessageToChannel(event,
                                              Sofia.seenLast(event.getUser().getNick(), key, seen.getUpdated().format(FORMATTER),
                                                             seen.getMessage()));
            } else {
                getBot().postMessageToChannel(event, Sofia.seenUnknown(event.getUser().getNick(), key));
            }
            return true;
        }
        return false;
    }
}