package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ShunDao;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Causes the bot to disregard bot triggers for a few minutes. Useful to de-fang abusive users without ejecting the bot from a channel
 * entirely.
 */
public class ShunOperation extends BotOperation {
    @Inject
    private ShunDao shunDao;

    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        if (message.startsWith("shun ")) {
            final String[] parts = message.substring(5).split(" ");
            if (parts.length == 0) {
                getBot().postMessageToChannel(event, Sofia.shunUsage());
            } else {
                getBot().postMessageToChannel(event, getShunnedMessage(parts));
            }
            return true;
        }
        return false;
    }

    private String getShunnedMessage(final String[] parts) {
        final String victim = parts[0];
        if (shunDao.isShunned(victim)) {
            return Sofia.alreadyShunned(victim);
        }
        final LocalDateTime until = parts.length == 1
                                    ? LocalDateTime.now().plusMinutes(5)
                                    : LocalDateTime.now().plusSeconds(Integer.parseInt(parts[1]));
        shunDao.addShun(victim, until);

        return String.format(Sofia.shunned(victim, new Date(until.toEpochSecond(ZoneOffset.UTC))));
    }
}