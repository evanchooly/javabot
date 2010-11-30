package javabot.commands;

import com.antwerkz.maven.SPI;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import javabot.operations.BotOperation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created Jan 26, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SPI({BotOperation.class, AdminCommand.class})
public class LockFactoid extends AdminCommand {
    @Param(primary = true)
    String name;
    @Autowired
    private FactoidDao dao;

    @Override
    public boolean canHandle(String message) {
        return message.startsWith("lock ") || message.startsWith("unlock ");
    }

    @Override
    public List<Message> execute(final Javabot bot, final BotEvent event) {
        final List<Message> responses = new ArrayList<Message>();
        final String command = args.get(0);
        final Factoid factoid = dao.getFactoid(name);
        if("lock".equals(command)) {
            factoid.setLocked(true);
            dao.save(factoid);
            responses.add(new Message(event.getChannel(), event, name + " locked."));
        } else if("unlock".equals(command)) {
            factoid.setLocked(false);
            dao.save(factoid);
            responses.add(new Message(event.getChannel(), event, name + " unlocked."));
        }
        return responses;
    }
}
