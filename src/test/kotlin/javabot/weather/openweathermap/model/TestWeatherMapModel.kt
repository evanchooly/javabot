package javabot.weather.openweathermap.model

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.test.junit.QuarkusTest
import javabot.BaseTest
import javabot.JavabotConfig
import javabot.dao.weather.openweathermap.model.OWWeather
import javabot.service.HttpService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestWeatherMapModel : BaseTest() {
    private val javabotConfig: JavabotConfig by lazy {
        injector.getInstance(JavabotConfig::class.java)
    }
    private val httpService: HttpService by lazy { injector.getInstance(HttpService::class.java) }

    @Test
    fun testOWMModelParsing() {
        val mapper = ObjectMapper()
        val place = "London, UK".replace(" ", "+")
        val apiUrl = "http://api.openweathermap.org/data/2.5/weather?q="
        val url = "$apiUrl$place&APPID=${javabotConfig.openweathermapToken()}"
        val weatherResponse = httpService.get(url)
        mapper.readValue(weatherResponse, OWWeather::class.java)
    }
}
