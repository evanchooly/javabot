package javabot.commands;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.javadoc.JavadocApi;

import javax.inject.Inject;

public class InfoApi extends AdminCommand {
    @Inject
    private ApiDao dao;

    @Param
    String name;

    @Override
    public void execute(final Message event) {
        final JavadocApi api = dao.find(name);
        if (api != null) {
            getBot().postMessageToChannel(event, Sofia.apiLocation(api.getName(), api.getBaseUrl()));
        } else {
            getBot().postMessageToChannel(event, Sofia.unknownApi(name, event.getUser().getNick()));
        }
    }
}