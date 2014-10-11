package javabot.commands;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;

import javax.inject.Inject;

@SPI({AdminCommand.class})
public class LockFactoid extends AdminCommand {
    @Param(primary = true)
    String name;

    @Inject
    private FactoidDao dao;

    @Override
    public boolean canHandle(String message) {
        return "lock".equals(message) || "unlock".equals(message);
    }

    @Override
    public void execute(final Message event) {
        final String command = args.get(0);
        if ("lock".equals(command) || "unlock".equals(command)) {
            final Factoid factoid = dao.getFactoid(name);
            if (factoid == null) {
                getBot().postMessage(event.getChannel(), event.getUser(), Sofia.factoidUnknown(name), event.isTell());
            } else if ("lock".equals(command)) {
                factoid.setLocked(true);
                dao.save(factoid);
                getBot().postMessage(event.getChannel(), event.getUser(), name + " locked.", event.isTell());
            } else if ("unlock".equals(command)) {
                factoid.setLocked(false);
                dao.save(factoid);
                getBot().postMessage(event.getChannel(), event.getUser(), name + " unlocked.", event.isTell());
            }
        }
    }
}
