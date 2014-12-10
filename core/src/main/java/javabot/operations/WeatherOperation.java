package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.Message;
import javabot.dao.impl.WeatherDao;
import javabot.model.Weather;

import javax.inject.Inject;

/**
 * Gets current weather conditions for a place given as a parameter.
 */
public class WeatherOperation extends BotOperation {
    @Inject
    private WeatherDao weatherDao;

    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        if (message.toLowerCase().startsWith("weather ")) {
            final String place = message.substring("weather ".length()).trim();
            final Weather result = weatherDao.getWeatherFor(place);
            if (result == null) {
                getBot().postMessage(event.getChannel(), event.getUser(), Sofia.weatherUnknown(place), event.isTell());
            } else {
                getBot().postMessage(event.getChannel(), event.getUser(), result.toString(), event.isTell());
            }
            return true;
        }
        return false;
    }

}
