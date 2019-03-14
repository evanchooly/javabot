package javabot.dao.impl

import javabot.JavabotConfig
import java.util.Arrays
import javabot.dao.impl.openweathermap.OpenWeatherMapHandler
import javabot.model.Weather

/**
 * Implements a weather service Dao using Weather Underground's Weather service, if that fails it tries Google's.
 *
 *
 * The user can also specify "using google for [place]" or "using wu for [place]" if they want to retrieve weather data
 * from a specific API

 */
class WeatherDao {

    //add more handlers to the list if there are other weather sources you want
    //the operation to support
    private val handlers = Arrays.asList(
            OpenWeatherMapHandler()
    )

    fun getWeatherFor(place: String, javabotConfig: JavabotConfig): Weather? {
        var weather: Weather? = null

        //Use each service until one returns something
        for (weatherDao in handlers) {
            weather = weatherDao.getWeatherFor(place, javabotConfig)
            if (weather != null) {
                break
            }
        }
        return weather
    }
}