package javabot;

import com.google.inject.Singleton;
import org.pircbotx.User;

@Singleton
public class TestJavabot extends Javabot {
/*
    @Override
    public String getNick() {
        return BaseTest.TEST_NICK;
    }

    @Override
    public void postAction(final org.pircbotx.Channel channel, String message) {
        postMessage(channel, null, message, false);
    }

    @Override
    public void postMessage(final org.pircbotx.Channel channel, final User user, String message, final boolean tell) {
        logMessage(channel, user, message);
        log.info(message);
        messages.add(new Message(channel, user, message, tell));
    }
*/

    public String getNick() {
        return BaseTest.TEST_BOT_NICK;
    }

    @Override
    public boolean isOnCommonChannel(final User user) {
        return true;
    }
}
