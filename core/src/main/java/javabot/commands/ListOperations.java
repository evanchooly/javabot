package javabot.commands;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ConfigDao;
import javabot.operations.BotOperation;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;

public class ListOperations extends OperationsCommand {
    @Inject
    private ConfigDao configDao;

    @Override
    public void execute(final Message event) {
        getBot().postMessageToChannel(event, Sofia.adminKnownOperations(event.getUser().getNick(),
                                                                 StringUtils.join(configDao.list(BotOperation.class).iterator(), ",")));

        listCurrent(event);
        getBot().postMessageToChannel(event, Sofia.adminOperationInstructions());
    }
}