package javabot.commands;

import com.antwerkz.maven.SPI;
import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.ChannelDao;
import javabot.dao.util.QueryParam;
import javabot.model.Channel;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@SPI({AdminCommand.class})
public class ListChannels extends AdminCommand {
    @Inject
    private ChannelDao dao;

    @Override
    public void execute(final Message event) {
        final List<Channel> channels = dao.find(new QueryParam(0, Integer.MAX_VALUE));
        getBot().postMessage(event.getChannel(), event.getUser(), Sofia.adminListChannelsPreamble(event.getUser().getNick())
                                , event.isTell());
        final List<String> names = channels.stream()
                                           .map(channel -> format("%s %s", channel.getName(), channel.getLogged() ? "(logged)" : ""))
                                           .collect(Collectors.toList());
        getBot().postMessage(null, event.getUser(), StringUtils.join(names, ", "), event.isTell());
    }
}