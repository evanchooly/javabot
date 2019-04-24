package javabot.dao.impl.openweathermap.model

import com.fasterxml.jackson.databind.ObjectMapper
import javabot.BaseTest
import javabot.JavabotConfig
import javabot.dao.weather.openweathermap.model.OWWeather
import org.apache.http.client.fluent.Request
import org.testng.annotations.Test
import javax.inject.Inject

@Test
class TestWeatherMapModel @Inject constructor(private val javabotConfig: JavabotConfig) : BaseTest(){
    fun testOWMModelParsing() {
        val mapper = ObjectMapper()
        val place = "London, UK".replace(" ", "+")
        val apiUrl = "http://api.openweathermap.org/data/2.5/weather?q="
        val url="$apiUrl$place&APPID=${javabotConfig.openweathermapToken()}"
        val weatherResponse = Request
                .Get(url)
                .execute()
                .returnContent()
        mapper.readValue(weatherResponse.asString(), OWWeather::class.java)
    }
}
