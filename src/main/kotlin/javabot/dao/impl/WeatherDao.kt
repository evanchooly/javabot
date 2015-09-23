package javabot.dao.impl

import java.util.Arrays
import java.util.HashMap

import javabot.dao.WeatherHandler
import javabot.model.Weather

/**
 * Implements a weather service Dao using Weather Underground's Weather service, if that fails it tries Google's.
 *
 *
 * The user can also specify "using google for [place]" or "using wu for [place]" if they want to retrieve weather data
 * from a specific API

 */
public class WeatherDao {

    //add more handlers to the list if there are other weather sources you want
    //the operation to support
    private val handlers = Arrays.asList(
          WeatherUndergroundHandler() as WeatherHandler)

    private val places = object : HashMap<String, String>() {
        init {
            put("NEW YORK", "New York City, NY")
            put("NYC", "New York City, NY")
            put("LAS VEGAS", "Las Vegas, NV")
            put("VEGAS", "Las Vegas, NV")
            put("LONDON", "London, United Kingdom")
            put("PARIS", "Paris, France")
            put("TORONTO", "Toronto, Ontario")
            put("RALEIGH", "Raleigh, NC")
        }
    }

    /**
     * Some of the weather services will interpret places like "Las Vegas" as a similar place in a country like
     * Mexico.  Since we'll probably have more people asking for weather in Las Vegas Nevada, the function will return
     * "Las Vegas, NV" when someone types in "Las Vegas".  If the user really wants Las Vegas Mexico, the user will have
     * to specify MX
     */
    private fun commonPlaces(place: String): String {
        val upper = place.toUpperCase()
        return if (places.containsKey(upper)) places.get(upper) else place

    }

    public fun getWeatherFor(place: String): Weather {
        var weather: Weather? = null

        //Use each service until one returns something
        for (weatherDao in handlers) {
            weather = weatherDao.getWeatherFor(commonPlaces(place))
            if (weather != null) {
                break
            }
        }
        return weather
    }
}