package javabot.dao;

import javabot.model.Weather;

public interface WeatherHandler {
  /**
   * Gets weather info, or null if none can be found
   *
   * @return Current Weather information, null if not found
   */
  Weather getWeatherFor(String place);

}
