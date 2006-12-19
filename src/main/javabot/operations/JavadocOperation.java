package javabot.operations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.JavadocParser;
import javabot.Message;
import org.jdom.JDOMException;

/**
 * @author ricky_clarkson
 */
public class JavadocOperation implements BotOperation {
    JavadocParser javadocParser;
    private final String javadocSources;
    private final String javadocBaseUrl;

    public JavadocOperation(String sources, String url) {
        javadocSources = sources;
        javadocBaseUrl = url;
    }

    /**
     * @see BotOperation#handleMessage(BotEvent)
     */
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();
        String message = event.getMessage();
        if(message.toLowerCase().startsWith("javadoc ")) {
            if(javadocParser == null) {
                try {
                    javadocParser = new JavadocParser(new File(javadocSources),javadocBaseUrl);
                } catch(IOException exception) {
                    throw new RuntimeException(exception);
                } catch(JDOMException exception) {
                    throw new RuntimeException(exception);
                }
            }
            String key = message.substring("javadoc ".length()).trim();
            String[] urls = javadocParser.javadoc(key);
            String sender = event.getSender();
            for(String url : urls) {
                messages.add(new Message(event.getChannel(), sender + ", please see " + url, false));
            }
            if(messages.isEmpty()) {
                messages.add(new Message(event.getChannel(),"I don't know of any documentation for " + key, false));
            }
        }
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}