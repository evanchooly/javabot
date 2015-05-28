package javabot.commands;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ConfigDao;

import javax.inject.Inject;

public class DisableOperation extends OperationsCommand {
    @Param
    String name;

    @Override
    public void execute(final Message event) {
        if (getBot().disableOperation(name)) {
            getBot().postMessageToChannel(event, Sofia.adminOperationDisabled(name));
            listCurrent(event);
        } else {
            getBot().postMessageToChannel(event, Sofia.adminOperationNotDisabled(name));
        }
    }
}