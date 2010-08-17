package javabot.operations;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.BotEvent;
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

    @Override
    public final List<Message> handleMessage(final BotEvent event) {
        String message = event.getMessage();
        final String channel = event.getChannel();
        final List<Message> responses = new ArrayList<Message>();
        if (message.startsWith(getTrigger())) {
            message = message.substring(getTrigger().length());
            try {
                responses.add(new Message(channel, event,
                    getBaseUrl() + URLEncoder.encode(message, Charset.defaultCharset().displayName())));
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }
        return responses;
    }

    protected abstract String getBaseUrl();

    protected abstract String getTrigger();
}