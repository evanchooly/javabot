package javabot.operations

import javabot.BaseMessagingTest
import javabot.operations.time.DateUtils
import org.testng.annotations.Test
import javax.inject.Inject

public class CurrentTimeOperationTest : BaseMessagingTest() {

    @Inject
    lateinit val dateUtils: DateUtils

    @Test
    @Throws(Exception::class)
    public fun getCurrentTimeForCountryWithSingleTimezone() {
        val command = "~time in japan"
        val timezone = "Asia/Tokyo"
        val expectedResponse = "Current time in Japan is ${dateUtils.getCurrentDateAtZone(timezone)}."
        scanForResponse(command, expectedResponse)
    }

    @Test
    @Throws(Exception::class)
    public fun getCurrentTimeForCountryWithMultipleTimezonesCanada() {
        val command = "~time in canada, winnipeg"
        val timezone = "America/Winnipeg"
        val expectedResponse = "Current time in Winnipeg is ${dateUtils.getCurrentDateAtZone(timezone)}."
        scanForResponse(command, expectedResponse)
    }

    @Test
    @Throws(Exception::class)
    public fun getCurrentTimeForCountryWithMultipleTimezonesUS() {
        val command = "~time in us, california"
        val timezone = "America/Los_Angeles"
        val expectedResponse = "Current time in California is ${dateUtils.getCurrentDateAtZone(timezone)}."
        scanForResponse(command, expectedResponse)
    }

    @Test
    @Throws(Exception::class)
    public fun getCurrentTimeInUnsupportedCity() {
        //Capital city on country with a single timezone is not supported yet
        val command = "~time in tokyo"
        val expectedResponse = "Location you entered is either not supported or does not exist."
        scanForResponse(command, expectedResponse)
    }

    @Test
    @Throws(Exception::class)
    public fun getCurrentTimeInInvalidCountry() {
        val command = "~time in mars"
        val expectedResponse = "Location you entered is either not supported or does not exist."
        scanForResponse(command, expectedResponse)
    }
}
