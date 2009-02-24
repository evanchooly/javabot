package javabot.operations;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javabot.Action;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GetFactoidOperation extends BotOperation {
    private static final Logger log = LoggerFactory.getLogger(GetFactoidOperation.class);
    @Autowired
    private FactoidDao factoidDao;

    public GetFactoidOperation(final Javabot bot) {
        super(bot);
    }

    @Override
    public boolean handleMessage(final BotEvent event) {
        final List<Message> messages = new ArrayList<Message>();
        getFactoid(event.getMessage(), event.getSender(), messages, event.getChannel(), event, new HashSet<String>());
        return true;
    }

    private void getFactoid(final String toFind, final String sender, final List<Message> messages,
        final String channel, final BotEvent event, final Set<String> backtrack) {
        String message = toFind;
        if (log.isDebugEnabled()) {
            log.debug(sender + " : " + message);
        }
        if (message.endsWith(".") || message.endsWith("?") || message.endsWith("!")) {
            message = message.substring(0, message.length() - 1);
        }
        final String firstWord = message.replaceAll(" .+", "");
        String params = message.replaceFirst("[^ ]+ ", "");
        String dollarOne = null;
        final String key = message;
        Factoid factoid = factoidDao.getFactoid(message.toLowerCase());
        if (factoid == null) {
            message = firstWord + " $1";
            factoid = factoidDao.getFactoid(message.toLowerCase());
            dollarOne = params;
        }
        if (factoid == null) {
            message = firstWord + " $+";
            factoid = factoidDao.getFactoid(message);
            dollarOne = urlencode(params);
        }
        if (factoid == null) {
            message = firstWord + " $^";
            factoid = factoidDao.getFactoid(message);
            dollarOne = urlencode(camelcase(params));
        }
        if (factoid != null) {
            processFactoid(sender, messages, channel, event, backtrack, dollarOne, key, factoid);
        } else {
            getBot().postMessage(new Message(channel, event, sender + ", I have no idea what " + toFind + " is."));
        }
    }

    private static String urlencode(String in) {
        try {
            return URLEncoder.encode(in, Charset.defaultCharset().displayName());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            return in;
        }
    }

    private static String camelcase(String in) {
        StringBuilder sb = new StringBuilder(in.replaceAll("\\s", " "));
        if (in.length() != 0) {
            int idx = sb.indexOf(" ");
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            while (idx > -1) {
                sb.deleteCharAt(idx);
                if (idx < sb.length()) {
                    sb.setCharAt(idx, Character.toUpperCase(sb.charAt(idx)));
                }
                idx = sb.indexOf(" ");
            }
        }
        return sb.toString();
    }

    private void processFactoid(final String sender, final List<Message> messages, final String channel,
        final BotEvent event, final Set<String> backtrack, final String replacedValue, final String key,
        final Factoid factoid) {
        String message;
        message = factoid.getValue();
        message = message.replaceAll("\\$who", sender);
        if (replacedValue != null) {
            message = message.replaceAll("\\$1", replacedValue);
            message = message.replaceAll("\\$\\+", replacedValue);
            message = message.replaceAll("\\$\\^", replacedValue);
        }
        message = processRandomList(message);
        if (message.startsWith("<see>")) {
            if (!backtrack.contains(message)) {
                backtrack.add(message);
                getFactoid(message.substring("<see>".length()).trim(), sender, messages, channel, event, backtrack);
            } else {
                getBot()
                    .postMessage(new Message(channel, event, "Reference loop detected for factoid '" + message + "'."));
            }
        } else if (message.startsWith("<reply>")) {
            getBot().postMessage(new Message(channel, event, message.substring("<reply>".length())));
        } else if (message.startsWith("<action>")) {
            getBot().postAction(new Action(channel, event, message.substring("<action>".length())));
        } else {
            getBot().postMessage(new Message(channel, event, sender + ", " + key + " is " + message));
        }
    }

    protected String processRandomList(final String message) {
        String result = message;
        int index = -1;
        index = result.indexOf("(", index + 1);
        int index2 = result.indexOf(")", index + 1);
        while (index < result.length() && index != -1 && index2 != -1) {
            final String choice = result.substring(index + 1, index2);
            final String[] choices = choice.split("\\|");
            if (choices.length > 1) {
                final int chosen = (int) (Math.random() * choices.length);
                result = String.format("%s%s%s", result.substring(0, index), choices[chosen],
                    result.substring(index2 + 1));
            }
            index = result.indexOf("(", index + 1);
            index2 = result.indexOf(")", index + 1);
        }
        return result;
    }
}