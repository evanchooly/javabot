package javabot.commands;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ConfigDao;
import javabot.model.Config;

import javax.inject.Inject;

public class EnableOperation extends OperationsCommand {
    @Param
    String name;

    @Inject
    private ConfigDao configDao;

    @Override
    public void execute(final Message event) {
        getBot().enableOperation(name);
        Config config = configDao.get();
        config.getOperations().add(name);
        configDao.save(config);
        getBot().postMessage(event.getChannel(), event.getUser(), Sofia.adminOperationEnabled(name), event.isTell());
        listCurrent(event);
    }
}
