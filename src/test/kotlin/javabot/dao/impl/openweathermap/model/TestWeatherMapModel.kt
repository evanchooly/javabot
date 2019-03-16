package javabot.dao.impl.openweathermap.model

import com.fasterxml.jackson.databind.ObjectMapper
import javabot.BaseTest
import javabot.JavabotConfig
import org.apache.http.client.fluent.Request
import org.testng.annotations.Test
import javax.inject.Inject


@Test
class TestWeatherMapModel @Inject constructor(private val javabotConfig: JavabotConfig) : BaseTest(){
    fun testOWMModelParsing() {
        val mapper = ObjectMapper()
        // this is the raw data from OWM's sample API: see
        // https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22
        val input = """
{
  "coord": {
    "lon": -0.13,
    "lat": 51.51
  },
  "weather": [
    {
      "id": 300,
      "main": "Drizzle",
      "description": "light intensity drizzle",
      "icon": "09d"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 280.32,
    "pressure": 1012,
    "humidity": 81,
    "temp_min": 279.15,
    "temp_max": 281.15
  },
  "visibility": 10000,
  "wind": {
    "speed": 4.1,
    "deg": 80
  },
  "clouds": {
    "all": 90
  },
  "dt": 1485789600,
  "sys": {
    "type": 1,
    "id": 5091,
    "message": 0.0103,
    "country": "GB",
    "sunrise": 1485762037,
    "sunset": 1485794875
  },
  "id": 2643743,
  "name": "London",
  "cod": 200
}
""".trimIndent()
        val weatherModel = mapper.readValue(input, OWWeather::class.java)
        println(weatherModel?.weather?.get(0)?.description)
        val place = "London, UK".replace(" ", "+")
        val API_URL = "http://api.openweathermap.org/data/2.5/weather?q="
        val url="$API_URL$place&APPID=${javabotConfig.openweathermapToken()}"
        println(url)
        val weatherResponse = Request
                .Get(url)
                .execute()
                .returnContent()
        println(weatherResponse)
        println(mapper.readValue(weatherResponse.asString(), OWWeather::class.java))
    }
}