package javabot.operations;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.LogDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

// Author : joed
// Date  : Apr 8, 2007

public class ChannelLogOperation implements BotOperation {

    private static Log log = LogFactory.getLog(ChannelLogOperation.class);

    private LogDao l_dao;

    public ChannelLogOperation(LogDao dao) {
        this.l_dao = dao;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        String message = event.getMessage();
        String channel = event.getChannel();
        String sender = event.getSender();

        l_dao.logMessage(sender, channel, message);

        return new ArrayList<Message>();
    }

    //Do nada
    public List<Message> handleMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}