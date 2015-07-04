package javabot.commands;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.javadoc.JavadocApi;
import org.pircbotx.Channel;

import javax.inject.Inject;

public class DropApi extends AdminCommand {
    @Inject
    private ApiDao dao;
    @Param
    String name;

    @Override
    public void execute(final Message event) {
        final JavadocApi api = dao.find(name);
        if (api != null) {
            drop(event, api);
        } else {
            getBot().postMessageToChannel(event, Sofia.unknownApi(name, event.getUser().getNick()));
        }
    }

    private void drop(final Message event, final JavadocApi api) {
        getBot().postMessageToChannel(event, Sofia.adminRemovingOldJavadoc(api.getName()));
        dao.delete(api);
        getBot().postMessageToChannel(event, Sofia.adminDoneRemovingOldJavadoc(api.getName()));
    }
}