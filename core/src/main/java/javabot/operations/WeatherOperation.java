package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import javabot.IrcEvent;
import javabot.Message;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.javadoc.Api;
import javabot.javadoc.Clazz;
import javabot.javadoc.Field;
import javabot.javadoc.Method;
import javabot.dao.WeatherDao;
import javabot.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;


/**
  * Gets current weather conditions for a place given as a parameter.
  *
  * @author Craig Tataryn <craiger@tataryn.net>
  */
@SPI(BotOperation.class)
public class WeatherOperation extends BotOperation {
    @Autowired
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
                    "The weather operation currently supports only places on Earth; thank you come again." + place));
            } else {
                String destination = event.getChannel();
                responses.add(new Message(destination, event, result.toString()));
            }
        }
        return responses;
    }

}
