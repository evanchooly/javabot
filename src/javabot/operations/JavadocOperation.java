package javabot.operations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.rickyclarkson.java.util.TypeSafeList;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.JavadocParser;
import javabot.Message;
import org.jdom.JDOMException;

/**
 * @author ricky_clarkson
 */
public class JavadocOperation implements BotOperation {
    JavadocParser javadocParser;

    /**
     * @see javabot.operations.BotOperation#handleMessage(javabot.BotEvent)
     */
    public List handleMessage(BotEvent event) {
        List messages = new TypeSafeList(new ArrayList(), Message.class);
        String message = event.getMessage();
        Javabot bot = event.getBot();
        if(message.toLowerCase().startsWith("javadoc ")) {
            if(javadocParser == null) {
                String javadocSources = bot.getJavadocSources();
                String javadocBaseUrl = bot.getJavadocBaseUrl();
                try {
                    System.out.println("javadocSources = " + javadocSources);
                    System.out.println("javadocBaseURL = " + javadocBaseUrl);
                    javadocParser = new JavadocParser
                        (new File(javadocSources),
                            javadocBaseUrl);
                } catch(IOException exception) {
                    throw new RuntimeException(exception);
                } catch(JDOMException exception) {
                    throw new RuntimeException(exception);
                }
            }
            String key = message.substring
                ("javadoc ".length()).trim();
            String[] urls = javadocParser.javadoc(key);
            String sender = event.getSender();
            for(int a = 0; a < urls.length; a++) {
                messages.add
                    (new Message
                        (event.getChannel(),
                            sender + ", please see " + urls[a],
                            false));
            }
            if(messages.size() == 0) {
                messages.add
                    (new Message
                        (event.getChannel(),
                            "I don't know of any " +
                    "documentation for " +
                    key,
                            false));
            }
        }
        return messages;
    }

    public List handleChannelMessage(BotEvent event)
    {
	    	return new TypeSafeList(new ArrayList(),Message.class);
    }
}
