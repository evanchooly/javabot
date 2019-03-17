package javabot.dao.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import javabot.JavabotConfig
import java.util.Arrays
import javabot.dao.impl.openweathermap.OpenWeatherMapHandler
import javabot.model.Weather

class WeatherDao @Inject constructor(javabotConfig: JavabotConfig) {
    companion object {
        val translations: Map<String, String> = WeatherDao::class.java
                .getResourceAsStream("/weatherTranslations.json")
                .use { inputStream ->
                    val mapper = ObjectMapper()
                    val typeReference = object : TypeReference<Map<String, String>>() {}
                    mapper.readValue<Map<String, String>>(
                            inputStream,
                            typeReference
                    )
                }
    }

    //add more handlers to the list if there are other weather sources you want
    //the operation to support
    private val handlers = Arrays.asList(
            OpenWeatherMapHandler(javabotConfig.openweathermapToken())
    )

    private fun placeTranslation(location: String): String {
        return if (location.contains(',')) {
            location
        } else {
            translations[location.toLowerCase()] ?: location
        }
    }

    fun getWeatherFor(place: String): Weather? {
        var weather: Weather? = null

        //Use each service until one returns something
        for (weatherDao in handlers) {
            weather = weatherDao.getWeatherFor(placeTranslation(place))
            if (weather != null) {
                break
            }
        }
        return weather
    }
}