package javabot.dao.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javabot.dao.WeatherHandler;
import javabot.model.Weather;

/**
 * Implements a weather service Dao using Weather Underground's Weather service, if that fails it tries Google's. <p>
 * The user can also specify "using google for [place]" or "using wu for [place]" if they want to retrieve weather data
 * from a specific API </p>
 *
 */
public class WeatherDao {

  //add more handlers to the list if there are other weather sources you want
  //the operation to support
  private final List<WeatherHandler> handlers = Arrays.asList(
      (WeatherHandler) new WeatherUndergroundHandler()
  );

  private Map<String, String> places = new HashMap<String, String>() {
      {
          put("NEW YORK", "New York City, NY");
          put("NYC", "New York City, NY");
          put("LAS VEGAS", "Las Vegas, NV");
          put("VEGAS", "Las Vegas, NV");
      }
  };

  /**
   * Some of the weather services will interpret places like "Las Vegas" as a similar place in a country like
   * Mexico.  Since we'll probably have more people asking for weather in Las Vegas Nevada, the function will return
   * "Las Vegas, NV" when someone types in "Las Vegas".  If the user really wants Las Vegas Mexico, the user will have
   * to specify MX
   */
  private String commonPlaces(String place) {
    String upper = place.toUpperCase();
    return places.containsKey(upper) ? places.get(upper) : place;

  }
  public Weather getWeatherFor(String place) {
    Weather weather = null;

    //Use each service until one returns something
    for (WeatherHandler weatherDao : handlers) {
      weather = weatherDao.getWeatherFor(commonPlaces(place));
      if (weather != null) {
        break;
      }
    }
    return weather;
  }
}