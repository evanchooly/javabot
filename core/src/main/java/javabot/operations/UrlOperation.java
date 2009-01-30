package javabot.operations;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created Dec 16, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public abstract class UrlOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(UrlOperation.class);

    public UrlOperation(final Javabot javabot) {
        super(javabot);
    }

    @Override
    public final boolean handleMessage(final BotEvent event) {
        String message = event.getMessage();
        final String channel = event.getChannel();
        boolean handled = false;
        if (message.startsWith(getTrigger())) {
            message = message.substring(getTrigger().length());
            try {
                getBot().postMessage(new Message(channel, event,
                    getBaseUrl() + URLEncoder.encode(message, Charset.defaultCharset().displayName())));
                handled = true;
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }
        return handled;
    }

    protected abstract String getBaseUrl();

    protected abstract String getTrigger();
}