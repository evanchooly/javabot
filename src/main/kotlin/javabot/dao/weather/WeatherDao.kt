package javabot.dao.weather

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import java.util.Arrays
import java.util.Locale
import javabot.JavabotConfig
import javabot.dao.geocode.GeocodeDao
import javabot.dao.weather.openweathermap.OpenWeatherMapHandler
import javabot.service.HttpService

class WeatherDao
@Inject
constructor(
    javabotConfig: JavabotConfig,
    private val geocodeDao: GeocodeDao,
    private val httpService: HttpService,
) {
    companion object {
        val translations: Map<String, String> =
            WeatherDao::class.java.getResourceAsStream("/weatherTranslations.json").use {
                inputStream ->
                val mapper = ObjectMapper()
                val typeReference = object : TypeReference<Map<String, String>>() {}
                mapper.readValue<Map<String, String>>(inputStream, typeReference)
            }
    }

    // add more handlers to the list if there are other weather sources you want
    // the operation to support
    private val handlers =
        Arrays.asList(
            OpenWeatherMapHandler(geocodeDao, javabotConfig.openweathermapToken(), httpService)
        )

    private fun placeTranslation(location: String): String {
        return if (location.contains(',')) {
            location
        } else {
            translations[location.lowercase(Locale.getDefault())] ?: location
        }
    }

    fun getWeatherFor(place: String): Weather? {
        var weather: Weather? = null

        // Use each service until one returns something
        for (weatherDao in handlers) {
            weather = weatherDao.getWeatherFor(placeTranslation(place))
            if (weather != null) {
                break
            }
        }
        return weather
    }
}
