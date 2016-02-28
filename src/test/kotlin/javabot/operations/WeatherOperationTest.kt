package javabot.operations

import com.google.inject.Inject
import javabot.BaseTest
import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Integration test for the Weather Operation, will actually attempt to contact the Google API for weather as
 * it's using the real Dao instead of a Mock/Stub
 */
class WeatherOperationTest : BaseTest() {
    @Inject
    private lateinit var operation: WeatherOperation
    @Test
    @Throws(Exception::class) fun tellWeather() {
        scanForResponse(operation.handleMessage(message("weather Winnipeg")), "Weather for")
    }

    @Test
    @Throws(Exception::class) fun zipCode() {
        scanForResponse(operation.handleMessage(message("weather 11217")), "Weather for")
    }

    @Test
    @Throws(Exception::class) fun cityNotFound() {
        scanForResponse(operation.handleMessage(message("weather lajdlfjlasjdf")), "only supports places on Earth")
    }

    @Test
    @Throws(Exception::class) fun cityWithSpaces() {
        scanForResponse(operation.handleMessage(message("weather New York")), "Weather for")
    }
}
