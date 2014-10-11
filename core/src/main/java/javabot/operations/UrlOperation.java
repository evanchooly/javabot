package javabot.operations;

import javabot.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public abstract class UrlOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(UrlOperation.class);

    @Override
    public boolean handleMessage(final Message event) {
        String message = event.getValue();
        if (message.startsWith(getTrigger())) {
            message = message.substring(getTrigger().length());
            try {
                getBot().postMessage(event.getChannel(), event.getUser(),
                                     getBaseUrl() + URLEncoder.encode(message, Charset.defaultCharset().displayName()),
                                     event.isTell());
                return true;
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }
        return false;
    }

    protected abstract String getBaseUrl();

    protected abstract String getTrigger();
}