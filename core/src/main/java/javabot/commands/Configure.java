package javabot.commands;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import static java.lang.String.format;
import javabot.IrcEvent;
import javabot.Javabot;
import javabot.Message;
import javabot.dao.ConfigDao;
import javabot.model.Config;
import org.apache.commons.lang.StringUtils;

@SPI({AdminCommand.class})
public class Configure extends AdminCommand {
  @Inject
  private ConfigDao dao;
  @Param(required = false)
  String property;
  @Param(required = false)
  String value;

  @Override
  public List<Message> execute(final Javabot bot, final IrcEvent event) {
    final List<Message> responses = new ArrayList<Message>();
    final Config config = dao.get();
    if (StringUtils.isEmpty(property)) {
      responses.add(new Message(event.getSender(), event, config.toString()));
    } else {
      try {
        final String name = property.substring(0, 1).toUpperCase() + property.substring(1);
        final Method get = config.getClass().getDeclaredMethod("get" + name);
        final Class<?> type = get.getReturnType();
        final Method set = config.getClass().getDeclaredMethod("set" + name, type);
        try {
          set.invoke(config, type.equals(String.class) ? value.trim() : Integer.parseInt(value));
          dao.save(config);
          bot.loadConfig();
          responses.add(new Message(event.getSender(), event, format("Setting %s to %s", property, value)));
        } catch (ReflectiveOperationException | NumberFormatException e) {
          responses.add(new Message(event.getSender(), event, e.getMessage()));
        }
      } catch (NoSuchMethodException e) {
        responses.add(new Message(event.getSender(), event, "I don't know of a property named " + property));
      }
    }
    return responses;
  }
}
