package javabot.operations;

import javax.inject.Inject;

import javabot.BaseMessagingTest;
import org.testng.annotations.Test;

import javabot.operations.time.DateUtils;
import javabot.operations.time.StringUtils;

public class CurrentTimeOperationTest extends BaseMessagingTest {

    @Inject
    private DateUtils dateUtils;
    @Inject
    private StringUtils stringUtils;

    @Test
    public void getCurrentTimeForCountryWithSingleTimezone( ) throws Exception {
        String command = "~time in japan";
        String timezone = "Asia/Tokyo";
        String expectedResponse = "Current time in Japan is " + dateUtils.getCurrentDateAtZone( timezone ) + ".";
        scanForResponse( command , expectedResponse );
    }

    @Test
    public void getCurrentTimeForCountryWithMultipleTimezonesCanada( ) throws Exception {
        String command = "~time in canada, winnipeg";
        String timezone = "America/Winnipeg";
        String expectedResponse = "Current time in Winnipeg is " + dateUtils.getCurrentDateAtZone( timezone ) + ".";
        scanForResponse( command , expectedResponse );
    }

    @Test
    public void getCurrentTimeForCountryWithMultipleTimezonesUS( ) throws Exception {
        String command = "~time in us, california";
        String timezone = "America/Los_Angeles";
        String expectedResponse = "Current time in California is " + dateUtils.getCurrentDateAtZone( timezone ) + ".";
        scanForResponse( command , expectedResponse );
    }

    @Test
    public void getCurrentTimeInUnsupportedCity( ) throws Exception {
        //Capital city on country with a single timezone is not supported yet
        String command = "~time in tokyo";
        String expectedResponse = "Location you entered is either not supported or does not exist.";
        scanForResponse( command , expectedResponse );
    }

    @Test
    public void getCurrentTimeInInvalidCountry( ) throws Exception {
        String command = "~time in mars";
        String expectedResponse = "Location you entered is either not supported or does not exist.";
        scanForResponse( command , expectedResponse );
    }
}
