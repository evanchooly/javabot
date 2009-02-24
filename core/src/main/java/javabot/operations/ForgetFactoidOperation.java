package javabot.operations;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ForgetFactoidOperation extends BotOperation {
    @Autowired
    private FactoidDao factoidDao;

    public ForgetFactoidOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        final String channel = event.getChannel();
        String message = event.getMessage();
        final String sender = event.getSender();

        boolean handled = false;
        if (message.startsWith("forget ")) {
            message = message.substring("forget ".length());
            if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
                message = message.substring(0, message.length() - 1);
            }
            final String key = message.toLowerCase();
            forget(event, channel, sender, key);
            handled = true;
        }
        return handled;
    }

    protected void forget(final BotEvent event, final String channel, final String sender, final String key) {
        if (factoidDao.hasFactoid(key)) {
            getBot().postMessage(new Message(channel, event, String.format("I forgot about %s, %s.", key, sender)));
            factoidDao.delete(sender, key);
        } else {
            getBot().postMessage(new Message(channel, event,
                String.format("I never knew about %s anyway, %s.", key, sender)));
        }
    }
}
