package javabot.dao.impl.openweathermap

import com.fasterxml.jackson.databind.ObjectMapper
import javabot.JavabotConfig
import javabot.dao.WeatherHandler
import javabot.dao.impl.openweathermap.model.OWWeather
import javabot.model.Weather
import net.iakovlev.timeshape.TimeZoneEngine
import org.apache.http.client.fluent.Request
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.xml.sax.InputSource
import java.net.URL
import java.net.URLEncoder
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Implements a weather service Dao using Weather Underground's weather API

 * @see WeatherDao
 */
class OpenWeatherMapHandler : WeatherHandler {
    override fun getWeatherFor(place: String, javabotConfig: JavabotConfig): Weather? {
        val mapper = ObjectMapper()
        return if (javabotConfig.openweathermapToken().isNotEmpty()) {
            try {
                val location = place.replace(" ", "+")
                val url = "$API_URL$location&APPID=${javabotConfig.openweathermapToken()}"

                val weatherResponse = Request
                        .Get(url)
                        .execute()
                        .returnContent().asString()
                val data = mapper.readValue<OWWeather>(weatherResponse, OWWeather::class.java)

                val weather = Weather()
                weather.city = "${data.name} (${data.sys!!.country})"
                weather.humidity = data.main!!.humidity.toString()
                weather.condition = data.weather!![0].description
                weather.wind = data.wind!!.speed.toString()
                val tempK = data.main!!.temp!!
                val tempC = tempK - 273.15
                val tempF = (tempC * 9.0 / 5 + 32.0)
                weather.tempc = "%4d".format(tempC.toInt()).trim()
                weather.tempf = "%4d".format(tempF.toInt()).trim()

                val zoneId = tzEngine.query(data.coord!!.lat!!, data.coord!!.lon!!)
                if (zoneId.isPresent) {
                    weather.localTime = ZonedDateTime.ofInstant(Instant.now(), zoneId.orElse(ZoneId.systemDefault())).format(DateTimeFormatter.RFC_1123_DATE_TIME)
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
        private const val API_URL = "http://api.openweathermap.org/data/2.5/weather?q="
        private val tzEngine: TimeZoneEngine = TimeZoneEngine.initialize()
    }
}
