package javabot.dao.impl;

import java.util.Arrays;
import java.util.List;

import javabot.dao.WeatherHandler;
import javabot.model.Weather;

/**
 * Implements a weather service Dao using Weather Underground's Weather service, if that fails it tries Google's. <p>
 * The user can also specify "using google for [place]" or "using wu for [place]" if they want to retrieve weather data
 * from a specific API </p>
 *
 * @author Craig Tataryn &lt;craiger@tataryn.net&gt;
 */
public class WeatherDao {
  private static final int WU = 0;

  private static final int GOOGLE = 1;

  private final List<WeatherHandler> handlers = Arrays.asList(
      new WeatherUndergroundHandler(),
      new GoogleWeatherHandler()
  );

  public Weather getWeatherFor(String place) {
    Weather weather = null;
    //if they ask for a specific service, use it
    String subPlace = place.toLowerCase();
    if (subPlace.startsWith("using google for ")) {
      return handlers.get(GOOGLE).getWeatherFor(place.substring(17));
    } else if (subPlace.startsWith("using wu for ")) {
      return handlers.get(WU).getWeatherFor(place.substring(13));
    }
    if(subPlace.matches("using .* for.*")) {
      place = subPlace.substring(subPlace.indexOf(" for ") + 5);
    }
    //if they don't want a specific service, then use each service until one returns something
    for (WeatherHandler weatherDao : handlers) {
      weather = weatherDao.getWeatherFor(place);
      if (weather != null) {
        break;
      }
    }
    return weather;
  }
}