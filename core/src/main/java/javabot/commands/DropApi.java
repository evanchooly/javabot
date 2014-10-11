package javabot.commands;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.javadoc.JavadocApi;
import org.pircbotx.Channel;

import javax.inject.Inject;

@SPI({AdminCommand.class})
public class DropApi extends AdminCommand {
    @Inject
    private ApiDao dao;
    @Param
    String name;

    @Override
    public void execute(final Message event) {
        final Channel channel = event.getChannel();
        final JavadocApi api = dao.find(name);
        if (api != null) {
            drop(event, channel, api);
        } else {
            getBot().postMessage(channel, event.getUser(), Sofia.unknownApi(name, event.getUser().getNick()), event.isTell());
        }
    }

    private void drop(final Message event, final Channel channel, final JavadocApi api) {
        getBot().postMessage(channel, event.getUser(), Sofia.adminRemovingOldJavadoc(api.getName()), event.isTell());
        dao.delete(api);
        getBot().postMessage(channel, event.getUser(), Sofia.adminDoneRemovingOldJavadoc(api.getName()), event.isTell());
    }
}