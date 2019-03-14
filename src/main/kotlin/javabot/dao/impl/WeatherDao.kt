package javabot.dao.impl

import com.google.inject.Inject
import javabot.JavabotConfig
import java.util.Arrays
import javabot.dao.impl.openweathermap.OpenWeatherMapHandler
import javabot.model.Weather

class WeatherDao @Inject constructor(private val javabotConfig: JavabotConfig) {
    //add more handlers to the list if there are other weather sources you want
    //the operation to support
    private val handlers = Arrays.asList(
            OpenWeatherMapHandler()
    )

    fun getWeatherFor(place: String): Weather? {
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