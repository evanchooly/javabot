package javabot.commands;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ConfigDao;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;

public class ListOperations extends OperationsCommand {
    @Inject
    private ConfigDao configDao;

    @Override
    public void execute(final Message event) {
        getBot().postMessage(event.getChannel(), event.getUser(),
                             Sofia.adminKnownOperations(event.getUser().getNick(), StringUtils.join(configDao.list().iterator(), ",")),
                             event.isTell());

        listCurrent(event);
        getBot().postMessage(event.getChannel(), event.getUser(), Sofia.adminOperationInstructions(), event.isTell());
    }
}