package javabot.commands;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.javadoc.JavadocApi;
import org.pircbotx.Channel;

import javax.inject.Inject;

public class InfoApi extends AdminCommand {
    @Inject
    private ApiDao dao;

    @Param
    String name;

    @Override
    public void execute(final Message event) {
        final Channel destination = event.getChannel();
        final JavadocApi api = dao.find(name);
        if (api != null) {
            getBot().postMessage(destination, event.getUser(), Sofia.apiLocation(api.getName(), api.getBaseUrl()), event.isTell());
        } else {
            getBot().postMessage(destination, event.getUser(), Sofia.unknownApi(name, event.getUser().getNick()), event.isTell());
        }
    }
}