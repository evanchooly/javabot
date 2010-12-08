package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;

@SPI(BotOperation.class)
public class StatsOperation extends BotOperation {
    @Autowired
    private FactoidDao factoidDao;

    private static final long startTime = System.currentTimeMillis();
    private int numberOfMessages = 0;

    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        numberOfMessages++;
        final String message = event.getMessage();
        final List<Message> responses = new ArrayList<Message>();
        if ("stats".equalsIgnoreCase(message)) {
            final long uptime = System.currentTimeMillis() - startTime;
            final long days = uptime / 86400000;
            responses.add(
                new Message(event.getChannel(), event, "I have been up for " + days + " days, have served "
                    + numberOfMessages + " messages, and have " + factoidDao.count() + " factoids."));
        }
        return responses;
    }
}