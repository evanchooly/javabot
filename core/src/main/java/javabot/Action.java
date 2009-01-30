package javabot;

import java.text.MessageFormat;

/**
 * Created Dec 15, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class Action extends Message {
    public Action(String dest, BotEvent evt, String value) {
        super(dest, evt, value);
    }

    @Override
    public String formatResponse(Javabot bot, String nick) {
        return MessageFormat.format("{0}, {1} {2}", nick, bot.getNick(), getMessage());
    }

    @Override
    public String logEntry() {
        return "did: " + getMessage();
    }

    @Override
    public void send(Javabot bot) {
        bot.postAction(this);
    }
}