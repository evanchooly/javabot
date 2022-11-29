package javabot.dao.weather.openweathermap

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.util.concurrent.RateLimiter
import javabot.dao.WeatherHandler
import javabot.dao.geocode.GeocodeDao
import javabot.dao.weather.Weather
import javabot.dao.weather.openweathermap.model.OWWeather
import javabot.service.HttpService
import net.iakovlev.timeshape.TimeZoneEngine
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.ZonedDateTime.ofInstant
import java.time.format.DateTimeFormatter

/**
 * Implements a weather service Dao using OpenWeather's weather API.
 * We may need to eventually think about OWM's terms of service; the free key allows
 * only 1000 requests a day. This may not be a problem in normal usage, but what
 * if a malicious bot or user issues 4000 weather requests? They'd get denied (the API
 * would reject them) but it's still impolite behavior on the part of the bot.
 *
 * @see javabot.dao.weather.WeatherDao
 */
class OpenWeatherMapHandler(
        private val geocodeDao: GeocodeDao, private val apiKey: String, val httpService: HttpService
) : WeatherHandler {

    companion object {
        private val API_URL = "http://api.openweathermap.org/data/2.5/weather"
        private val tzEngine: TimeZoneEngine = TimeZoneEngine.initialize()
        val LOG: Logger = LoggerFactory.getLogger(OpenWeatherMapHandler::class.java)
    }

    // allow one a second - it's actually 60/sec on OWM but who cares
    @Suppress("UnstableApiUsage")
    private val rateLimiter = RateLimiter.create(60.0)

    override fun getWeatherFor(place: String): Weather? {
        return if (apiKey.isNotEmpty()) {
            if (rateLimiter.tryAcquire()) {
                val mapper = ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                try {
                    val location = place.replace(" ", "+")
                    // will throw an exception if the lat/long is empty. We WANT this. It'll exit cleanly.
                    val loc = geocodeDao.getLatitudeAndLongitudeFor(location) ?: return null

                    val url = "$API_URL?APPID=$apiKey&lat=${loc.latitude}&lon=${loc.longitude}"

                    val weatherResponse = httpService.get(url)
                    val data = mapper.readValue<OWWeather>(weatherResponse, OWWeather::class.java)

                    val weather = Weather(
                            city = loc.address,
                            humidity = data.main!!.humidity.toString(),
                            condition = data.weather!![0].description,
                            wind = data.wind!!.speed.toString(),
                            tempCelsius = data.main!!.temp!! - 273.15
                    )

                    val zoneId = tzEngine.query(data.coord!!.lat!!, data.coord!!.lon!!)
                    zoneId.ifPresent {
                        weather.localTime = ofInstant(Instant.now(), it).format(DateTimeFormatter.RFC_1123_DATE_TIME)
                    }
                    weather
                } catch (e: Throwable) {
                    LOG.debug(e.message, e)
                    null
                }
            } else {
                null
            }
        } else {
            null
        }
    }
}
