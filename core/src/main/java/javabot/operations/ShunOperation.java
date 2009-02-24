package javabot.operations;

import java.util.Date;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ShunDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Causes the bot to disregard bot triggers for a few minutes. Useful to de-fang abusive users without ejecting the bot
 * from a channel entirely.
 */
public class ShunOperation extends BotOperation {
    private static final long MILLISECOND = 1;
    private static final long SECOND = 1000 * MILLISECOND;
    private static final long MINUTE = 60 * SECOND;
    private static final long SHUN_DURATION = 5 * MINUTE;
    @Autowired
    private ShunDao shunDao;

    public ShunOperation(final Javabot javabot) {
        super(javabot);
    }

    public boolean handleMessage(final BotEvent event) {
        final String message = event.getMessage();
        final String[] parts = message.split(" ");
        if (parts.length == 2 && "shun".equals(parts[0])) {
            getBot().postMessage(new Message(event.getChannel(), event, getShunnedMessage(parts[1])));
            return true;
        }
        return false;
    }

    private Date calculateShunExpiry() {
        return new Date(System.currentTimeMillis() + SHUN_DURATION);
    }

    private String getShunnedMessage(final String victim) {
        if (shunDao.isShunned(victim)) {
            return String.format("%s is already shunned.", victim);
        }
        final Date until = calculateShunExpiry();
        shunDao.addShun(victim, until);
        return String.format("%s is shunned until %2$tY/%2$tm/%2$td %2$tH:%2$tM:%2$tS.", victim, until);
    }
}