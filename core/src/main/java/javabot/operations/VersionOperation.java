package javabot.operations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;

@SPI(StandardOperation.class)
public class VersionOperation extends StandardOperation {
    @Override
    public List<Message> handleMessage(final IrcEvent event) {
        final String message = event.getMessage();
        final List<Message> responses = new ArrayList<Message>();
        if ("version".equalsIgnoreCase(message)) {
            responses.add(new Message(event.getChannel(), event, getBot().loadVersion()));
        }
        return responses;
    }
}