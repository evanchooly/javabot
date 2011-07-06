package javabot.dao;

import javabot.model.Weather;
/**
 * Interface for getting Weather info from a service
 *
 * @author Craig Tataryn &lt;craiger@tataryn.net&gt;
 */
public interface WeatherDao {

    /**
     * Gets weather info, or null if none can be found
     * @return Current Weather information, null if not found
     */
    public Weather getWeatherFor(String place);

}
