package javabot.operations

import javabot.BaseMessagingTest
import org.testng.annotations.Test

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

import org.testng.Assert.assertEquals

/**
 * Integration test for the Weather Operation, will actually attempt to contact the Google API for weather as
 * it's using the real Dao instead of a Mock/Stub
 */
public class WeatherOperationTest : BaseMessagingTest() {
    @Test
    @Throws(Exception::class)
    public fun tellWeather() {
        scanForResponse("~weather Winnipeg", "Weather for")
    }

    @Test
    @Throws(Exception::class)
    public fun zipCode() {
        scanForResponse("~weather 11217", "Weather for")
    }

    @Test
    @Throws(Exception::class)
    public fun cityNotFound() {
        scanForResponse("~weather lajdlfjlasjdf", "only supports places on Earth")
    }

    @Test
    @Throws(Exception::class)
    public fun cityWithSpaces() {
        super.scanForResponse("~weather New York", "Weather for")

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

    /**
     * This method is useful for validating the weather
     * output from the service. Not normally predictable...
     * which is a nice way of saying the code author
     * (jottinger) is a lazy git who doesn't feel like
     * working it out.
     */
    @SuppressWarnings("unused")
    public fun validateWeatherTime() {
        super.testMessage("~weather tel aviv", "foo")
    }

}
