package javabot.operations;

import javabot.BaseMessagingTest;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.testng.Assert.assertEquals;

/**
 * Integration test for the Weather Operation, will actually attempt to contact the Google API for weather as
 * it's using the real Dao instead of a Mock/Stub
 */
public class WeatherOperationTest extends BaseMessagingTest {
    @Test
    public void tellWeather() throws Exception {
        scanForResponse("~weather Winnipeg", "Weather for");
    }

    @Test
    public void zipCode() throws Exception {
        scanForResponse("~weather 11217", "Weather for");
    }

    @Test
    public void cityNotFound() throws Exception {
        scanForResponse("~weather lajdlfjlasjdf", "only supports places on Earth");
    }

    @Test
    public void cityWithSpaces() throws Exception {
        super.scanForResponse("~weather New York", "Weather for");

    }

    @Test
    public void parseDateTime() throws ParseException {
        String dateString = "May 13, 4:57 PM IDT";
        SimpleDateFormat in = new SimpleDateFormat("MMM dd, hh:mm a zzz");
        SimpleDateFormat out = new SimpleDateFormat("h:mm a");
        Date date = in.parse(dateString);
        assertEquals("9:57 AM", out.format(date));
    }

    /**
     * This method is useful for validating the weather
     * output from the service. Not normally predictable...
     * which is a nice way of saying the code author
     * (jottinger) is a lazy git who doesn't feel like
     * working it out.
     */
    //@Test
    @SuppressWarnings("unused")
    public void validateWeatherTime() {
        super.testMessage("~weather toronto", "foo");
    }

}
