package javabot;

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
    public void send(Javabot bot) {
        bot.postAction(this);
    }
}