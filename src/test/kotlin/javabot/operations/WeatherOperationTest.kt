package javabot.operations

import com.google.inject.Inject
import javabot.BaseTest
import org.testng.Assert
import org.testng.annotations.Test

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

import org.testng.Assert.assertEquals

/**
 * Integration test for the Weather Operation, will actually attempt to contact the Google API for weather as
 * it's using the real Dao instead of a Mock/Stub
 */
public class WeatherOperationTest : BaseTest() {
    @Inject
    private lateinit var operation: WeatherOperation
    @Test
    @Throws(Exception::class)
    public fun tellWeather() {
        scanForResponse(operation.handleMessage(message("weather Winnipeg")), "Weather for")
    }

    @Test
    @Throws(Exception::class)
    public fun zipCode() {
        scanForResponse(operation.handleMessage(message("weather 11217")), "Weather for")
    }

    @Test
    @Throws(Exception::class)
    public fun cityNotFound() {
        scanForResponse(operation.handleMessage(message("weather lajdlfjlasjdf")), "only supports places on Earth")
    }

    @Test
    @Throws(Exception::class)
    public fun cityWithSpaces() {
        scanForResponse(operation.handleMessage(message("weather New York")), "Weather for")
    }

    @Test
    @Throws(ParseException::class)
    public fun parseDateTime() {
        val dateString = "May 13, 4:57 PM IDT"
        val `in` = SimpleDateFormat("MMM dd, hh:mm a zzz")
        val out = SimpleDateFormat("h:mm a")
        val date = `in`.parse(dateString)
        assertEquals("9:57 AM", out.format(date))
    }
}
