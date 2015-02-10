package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.model.Logs;
import javabot.model.criteria.LogsCriteria;
import org.mongodb.morphia.Datastore;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;

public class LogsOperation extends BotOperation {
    private static final String KEYWORD_LOGS = "logs";

    @Inject
    private Datastore ds;

    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        if (message.toLowerCase().startsWith(KEYWORD_LOGS)) {
            final String nickname = message.substring(KEYWORD_LOGS.length()).trim();
            LogsCriteria criteria = new LogsCriteria(ds);
            criteria.channel(event.getChannel().getName());
            criteria.updated().order(true);
            boolean handled = false;
            if (nickname.isEmpty()) {
                criteria.query().limit(200);
            } else {
                criteria.nick(nickname);
                criteria.query().limit(50);
            }
            for (Logs logs : criteria.query().fetch()) {
                getBot().postMessage(event.getChannel(), event.getUser(),
                                     Sofia.logsEntry(logs.getUpdated().format(DateTimeFormatter.ofPattern("HH:mm")),
                                                     logs.getNick(), logs.getMessage()), event.isTell());
                handled = true;
            }
            if (!handled) {
                if (nickname.isEmpty()) {
                    getBot().postMessage(event.getChannel(), event.getUser(), Sofia.logsNone(), event.isTell());
                } else {
                    getBot().postMessage(event.getChannel(), event.getUser(), Sofia.logsNoneForNick(nickname), event.isTell());
                }
            }
            return true;
        }
        return false;
    }
}
