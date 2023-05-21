package javabot.operations

import com.google.inject.Inject
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javabot.BaseTest
import javabot.JavabotConfig
import org.testng.SkipException
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

/**
 * Integration test for the Weather Operation, will actually attempt to contact the Google API for
 * weather as it's using the real Dao instead of a Mock/Stub
 */
class WeatherOperationTest : BaseTest() {
    @Inject private lateinit var operation: WeatherOperation
    @Inject private lateinit var config: JavabotConfig

    @Test
    fun tellWeather() {
        scanForResponse(operation.handleMessage(message("~weather Winnipeg")), "Weather for")
    }

    @Test
    fun zipCode() {
        scanForResponse(operation.handleMessage(message("~weather 11217")), "Weather for")
    }

    @Test
    fun cityNotFound() {
        // we have to use a genuinely odd name, because google maps matches a LOT of place names
        scanForResponse(
            operation.handleMessage(message("~weather zila jdlfj lasjdf")),
            "only supports places on Earth"
        )
    }

    @Test
    fun cityShowTimezone() {
        val messages = operation.handleMessage(message("~weather Phoenix, AZ"))
        scanForResponse(messages, "Weather for")
        scanForResponse(messages, "-0700") // PHX doesn't have DST yet, so this is constant
    }

    @BeforeTest
    fun checkForToken() {
        if (config.openweathermapToken() == "") {
            throw SkipException("weather token not configured")
        }
    }

    @Test
    fun cityWithSpaces() {
        val messages = operation.handleMessage(message("~weather New York"))
        scanForResponse(messages, "Weather for")
        val o = ZoneId.of("America/New_York")
        val zdt = ZonedDateTime.now(o)
        val zo = zdt.format(DateTimeFormatter.ofPattern("Z"))
        scanForResponse(messages, zo)
    }

    @Test
    fun cityUseTranslation() {
        var messages = operation.handleMessage(message("~weather brooklyn"))
        scanForResponse(messages, "Weather for")
        scanForResponse(messages, "Brooklyn, NY, USA")
    }
}
