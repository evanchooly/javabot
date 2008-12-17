package javabot.operations;

import java.util.List;
import java.util.ArrayList;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.io.UnsupportedEncodingException;

import javabot.Javabot;
import javabot.Message;
import javabot.BotEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created Dec 16, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public abstract class UrlOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(UrlOperation.class);
    
    public UrlOperation(Javabot javabot) {
        super(javabot);
    }

    @Override
    public final List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        if (!message.startsWith(getTrigger())) {
            return messages;
        }
        message = message.substring(getTrigger().length());
        try {
            messages.add(new Message(channel, event,
                getBaseUrl() + URLEncoder.encode(message, Charset.defaultCharset().displayName())));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return messages;
    }

    protected abstract String getBaseUrl();

    protected abstract String getTrigger();

    @Override
    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
