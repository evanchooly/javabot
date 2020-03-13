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
    fun tellWeather() {
        scanForResponse(operation.handleMessage(message("~weather Winnipeg")), "Weather for")
    }

    @Test
    fun zipCode() {
        scanForResponse(operation.handleMessage(message("~weather 11217")), "Weather for")
    }

    @Test
    fun cityNotFound() {
        scanForResponse(operation.handleMessage(message("~weather lajdlfjlasjdf")), "only supports places on Earth")
    }

    @Test
    fun cityShowTimezone() {
        val messages = operation.handleMessage(message("~weather London"))
        scanForResponse(messages, "Weather for")
        scanForResponse(messages, "GMT")
    }

    @Test
    fun cityWithSpaces() {
        val messages = operation.handleMessage(message("~weather New York"))
        scanForResponse(messages, "Weather for")
        scanForResponse(messages, "-0400")
    }

    @Test
    fun cityUseTranslation() {
        var messages = operation.handleMessage(message("~weather brooklyn"))
        scanForResponse(messages, "Weather for")
        scanForResponse(messages, "Brooklyn, NY, USA")
    }
}
