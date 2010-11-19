package javabot.commands;

import com.antwerkz.maven.SPI;
import javabot.BotEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created Jan 26, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SPI(Command.class)
public class LockFactoid extends OperationsCommand {
//    @Param
//    String name;
    @Autowired
    private FactoidDao dao;

    @Override
    public void execute(final List<Message> responses, final Javabot bot, final BotEvent event) {
        final String message = event.getMessage();
        final String command = message.substring(0, message.indexOf(" "));
        final String name = message.substring(message.indexOf(" ")).trim();
        final Factoid factoid = dao.getFactoid(name);
        if("lockFactoid".equals(command)) {
            factoid.setLocked(true);
            responses.add(new Message(event.getChannel(), event, name + " locked."));
            dao.save(factoid);
        } else if("unlockFactoid".equals(command)) {
            factoid.setLocked(false);
            responses.add(new Message(event.getChannel(), event, name + " unlocked."));
        }
    }
}
