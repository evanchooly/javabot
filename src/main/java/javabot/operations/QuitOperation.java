package javabot.operations;

import javabot.Message;
import javabot.dao.ConfigDao;

import javax.inject.Inject;

public class QuitOperation extends BotOperation {

    @Inject
    private ConfigDao configDao;

    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        if (message.toLowerCase().startsWith("quit ")) {
            if (message.substring("quit ".length()).equals(configDao.get().getPassword())) {
                System.exit(0);
            }
            return true;
        }
        return false;
    }
}