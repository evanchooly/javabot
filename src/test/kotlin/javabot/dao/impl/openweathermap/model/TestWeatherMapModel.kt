package javabot.dao.impl.openweathermap.model

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.inject.Inject
import javabot.BaseTest
import javabot.JavabotConfig
import javabot.dao.weather.openweathermap.model.OWWeather
import javabot.service.HttpService
import org.testng.annotations.Test

@Test
class TestWeatherMapModel
@Inject
constructor(private val javabotConfig: JavabotConfig, private val httpService: HttpService) :
    BaseTest() {
    fun testOWMModelParsing() {
        val mapper = ObjectMapper()
        val place = "London, UK".replace(" ", "+")
        val apiUrl = "http://api.openweathermap.org/data/2.5/weather?q="
        val url = "$apiUrl$place&APPID=${javabotConfig.openweathermapToken()}"
        val weatherResponse = httpService.get(url)
        mapper.readValue(weatherResponse, OWWeather::class.java)
    }
}
