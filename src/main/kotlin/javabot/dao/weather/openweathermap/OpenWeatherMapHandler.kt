package javabot.dao.weather.openweathermap

import com.fasterxml.jackson.databind.ObjectMapper
import javabot.dao.WeatherHandler
import javabot.dao.geocode.GeocodeDao
import javabot.dao.weather.openweathermap.model.OWWeather
import javabot.model.Weather
import net.iakovlev.timeshape.TimeZoneEngine
import org.apache.http.client.fluent.Request
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Implements a weather service Dao using OpenWeather's weather API.
 * We may need to eventually think about OWM's terms of service; the free key allows
 * only 1000 requests a day. This may not be a problem in normal usage, but what
 * if a malicious bot or user issues 4000 weather requests? They'd get denied (the API
 * would reject them) but it's still impolite behavior on the part of the bot.
 *
 * @see javabot.dao.impl.WeatherDao
 */
class OpenWeatherMapHandler(
        private val geocodeDao: GeocodeDao,
        private val apiKey: String
) : WeatherHandler {
    override fun getWeatherFor(place: String): Weather? {
        val mapper = ObjectMapper()
        return if (apiKey.isNotEmpty()) {
            try {
                val location = place.replace(" ", "+")
                // will throw an exception if the lat/long is empty. We WANT this. It'll exit cleanly.
                val loc = geocodeDao.getLatitudeAndLongitudeFor(location) ?: throw Exception()
                val url = "$API_URL?APPID=$apiKey&lat=${loc.latitude}&lon=${loc.longitude}"

                val weatherResponse = Request
                        .Get(url)
                        .execute()
                        .returnContent().asString()
                val data = mapper.readValue<OWWeather>(weatherResponse, OWWeather::class.java)
                val zoneId = tzEngine.query(data.coord!!.lat!!, data.coord!!.lon!!)

                val weather = Weather(
                        city = loc.address,
                        humidity = data.main!!.humidity.toString(),
                        condition = data.weather!![0].description,
                        wind = data.wind!!.speed.toString(),
                        tempCelsius = data.main!!.temp!! - 273.15
                )

                if (zoneId.isPresent) {
                    val weatherTime = ZonedDateTime.ofInstant(
                            Instant.now(),
                            zoneId.orElse(ZoneId.systemDefault())
                    )
                    weather.localTime = weatherTime.format(DateTimeFormatter.RFC_1123_DATE_TIME)
                }
                weather
            } catch (e: Throwable) {
                null
            }
        } else {
            null
        }
    }

    companion object {
        private const val API_URL = "http://api.openweathermap.org/data/2.5/weather"
        private val tzEngine: TimeZoneEngine = TimeZoneEngine.initialize()
    }
}
