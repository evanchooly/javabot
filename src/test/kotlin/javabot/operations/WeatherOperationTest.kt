package javabot.operations

import com.google.inject.Inject
import javabot.BaseTest
import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
        // we have to use a genuinely odd name, because google maps matches a LOT of place names
        scanForResponse(operation.handleMessage(message("~weather zila jdlfj lasjdf")), "only supports places on Earth")
    }

    @Test
    fun cityShowTimezone() {
        val messages = operation.handleMessage(message("~weather Phoenix, AZ"))
        scanForResponse(messages, "Weather for")
        scanForResponse(messages, "-0700") // PHX doesn't have DST yet, so this is constant
    }

    @Test
    fun cityWithSpaces() {
        val messages = operation.handleMessage(message("~weather New York"))
        scanForResponse(messages, "Weather for")
        val offset = if (TimeZone.getTimeZone("America/New York").inDaylightTime(Date())) {
            "-0400" // EDT
        } else {
            "-0500" // EST
        }
        scanForResponse(messages, offset)
    }

    @Test
    fun cityUseTranslation() {
        var messages = operation.handleMessage(message("~weather brooklyn"))
        scanForResponse(messages, "Weather for")
        scanForResponse(messages, "Brooklyn, NY, USA")
    }
}
