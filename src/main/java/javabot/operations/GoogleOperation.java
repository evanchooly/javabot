package javabot.operations;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;
import java.io.UnsupportedEncodingException;

import javabot.BotEvent;
import javabot.Message;
import javabot.Javabot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ricky_clarkson
 */
public class GoogleOperation extends BotOperation {
    private static final String PLAIN_GOOGLE = "http://www.google.com/search?q=";
    private static final Logger log = LoggerFactory.getLogger(GoogleOperation.class);

    public GoogleOperation(Javabot javabot) {
        super(javabot);
    }

    @Override
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        String channel = event.getChannel();
        if (!message.startsWith("google ")) {
            return messages;
        }
        message = message.substring("google ".length());
        try {
            messages.add(new Message(channel, event,
                PLAIN_GOOGLE + URLEncoder.encode(message, Charset.defaultCharset().displayName())));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return messages;
    }

    @Override
    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}