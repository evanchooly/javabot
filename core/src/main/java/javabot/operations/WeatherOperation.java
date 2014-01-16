package javabot.operations;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.dao.impl.WeatherDao;
import javabot.model.Weather;

/**
 * Gets current weather conditions for a place given as a parameter.
 *
 * @author Craig Tataryn &lt;craiger@tataryn.net&gt;
 */
@SPI(BotOperation.class)
public class WeatherOperation extends BotOperation {
  @Inject
  private WeatherDao weatherDao;

  @Override
  public List<Message> handleMessage(final IrcEvent event) {
    final String message = event.getMessage();
    final List<Message> responses = new ArrayList<Message>();
    if (message.toLowerCase().startsWith("weather ")) {
      final String place = message.substring("weather ".length()).trim();
      final Weather result = weatherDao.getWeatherFor(place);
      if (result == null) {
        responses.add(new Message(event.getChannel(), event,
            "We cannot locate " +  place + ". The weather operation only supports places on Earth; thank you come again."));
      } else {
        responses.add(new Message(event.getChannel(), event, result.toString()));
      }
    }
    return responses;
  }

}
