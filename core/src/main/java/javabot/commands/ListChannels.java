package javabot.commands;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ChannelDao;
import javabot.dao.util.QueryParam;
import javabot.model.Channel;
import org.apache.commons.lang.StringUtils;

/**
 * Created Dec 17, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@SPI({AdminCommand.class})
public class ListChannels extends AdminCommand {
  @Inject
  private ChannelDao dao;

  @Override
  public List<Message> execute(final Javabot bot, final IrcEvent event) {
    final List<Message> responses = new ArrayList<Message>();
    final List<Channel> channels = dao.find(new QueryParam(0, Integer.MAX_VALUE));
    responses.add(new Message(event.getChannel(), event, event.getSender() + ", I'll list the channels in a"
        + " private message for you"));
    final List<String> chans = new ArrayList<String>();
    for (final Channel channel : channels) {
      chans.add(String.format("%s %s", channel.getName(), channel.getLogged() ? "(logged)" : ""));
    }
    responses.add(new Message(event.getSender().getNick(), event, StringUtils.join(chans, ", ")));
    return responses;
  }
}