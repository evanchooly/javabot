package javabot.commands;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ConfigDao;

import javax.inject.Inject;

public class EnableOperation extends OperationsCommand {
    @Param
    String name;

    @Override
    public void execute(final Message event) {
        getBot().enableOperation(name);
        getBot().postMessageToChannel(event, Sofia.adminOperationEnabled(name));
        listCurrent(event);
    }
}
