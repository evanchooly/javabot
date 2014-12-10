package javabot.commands;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ConfigDao;
import javabot.model.Config;

import javax.inject.Inject;

public class DisableOperation extends OperationsCommand {
    @Param
    String name;

    @Inject
    private ConfigDao configDao;

    @Override
    public void execute(final Message event) {
        if (getBot().disableOperation(name)) {
            getBot().postMessage(event.getChannel(), event.getUser(), Sofia.adminOperationDisabled(name), event.isTell());
            listCurrent(event);
        } else {
            getBot().postMessage(event.getChannel(), event.getUser(), Sofia.adminOperationNotDisabled(name), event.isTell());
        }
    }
}