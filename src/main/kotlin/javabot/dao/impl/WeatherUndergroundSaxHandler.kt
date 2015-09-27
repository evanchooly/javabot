package javabot.dao.impl

import javabot.model.Weather
import org.apache.commons.lang.StringUtils
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.util.HashMap
import java.util.HashSet

public class WeatherUndergroundSaxHandler : DefaultHandler() {
    var collectLocationData = false
    var collectWeatherData = false
    var currentElem = ""
    var error = false

    private val weatherMap = HashMap<String, String>()
    private val weatherElems = object : HashSet<String>() {
        init {
            add("temp_f")
            add("temp_c")
            add("weather")
            add("wind_string")
            add("relative_humidity")
            add("windchill_string")
            add("local_time")
        }
    }

    @Throws(SAXException::class)
    override fun startElement(uri: String?, localName: String, qName: String, attributes: Attributes?) {
        if ("display_location".equals(qName, true)) {
            collectLocationData = true
            collectWeatherData = false
        } else if ("current_observation".equals(qName, true)) {
            collectWeatherData = true
        }

        if ((collectLocationData || collectWeatherData)) {
            currentElem = qName
        }
    }

    @Throws(SAXException::class)
    override fun endElement(uri: String?, localName: String, qName: String) {
        if ("display_location".equals(qName, true)) {
            collectLocationData = false
            collectWeatherData = true
        } else if ("current_observation".equals(qName, true)) {
            collectWeatherData = false
        }
        currentElem = ""
    }

    @Throws(SAXException::class)
    override fun characters(chars: CharArray?, start: Int, length: Int) {
        val body = StringUtils.trimToEmpty(StringBuffer().append(chars, start, length).toString())
        if (!error) {
            if (collectWeatherData && weatherElems.contains(currentElem)) {
                //In certain cases WU will display "NA" for elements that don't have a value
                weatherMap.put(currentElem, if (StringUtils.trimToEmpty(body) == "NA") "" else body)
            } else if (collectLocationData && "full" == currentElem) {
                weatherMap.put(currentElem, body)
            }
        }
    }

    /**
     * Returns the weather data parsed from the feed.

     * @return Weather data, or null if none was parsed
     */
    public fun getWeather(): Weather? {
        error = error || weatherMap.get("full") == ","
        if (error) {
            //TODO Log
            return null
        } else {
            val weather = Weather()
            weather.city = weatherMap.get("full")
            weather.condition = weatherMap.get("weather")
            weather.tempf = weatherMap.get("temp_f")
            weather.tempc = weatherMap.get("temp_c")
            weather.humidity = weatherMap.get("relative_humidity")
            weather.wind = weatherMap.get("wind_string")
            weather.windChill = weatherMap.get("windchill_string")

            val localTime = weatherMap.get("local_time")
            if (localTime != null && "" != localTime.trim()) {
                val tokens = localTime.trim().split(" ")
                if (tokens.size() == 5) {
                    val time = StringBuilder()
                    time.append(tokens[2]).append(" ").append(tokens[3])
                    weather.localTime = time.toString()
                }
            }
            return weather
        }
    }
}
