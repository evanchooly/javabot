package javabot.dao

import javabot.model.Weather

interface WeatherHandler {
    /**
     * Gets weather info, or null if none can be found
     * @return Current Weather information, null if not found
     */
    fun getWeatherFor(place: String): Weather?

}
