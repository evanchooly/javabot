package javabot.operations;

import org.testng.annotations.Test;

/**
 * Integration test for the Weather Operation, will actually attempt to contact the Google API for weather as
 * it's using the real Dao instead of a Mock/Stub
 *
 * @author Craig Tataryn &lt;craiger@tataryn.net&gt;
 */
public class WeatherOperationTest extends BaseOperationTest {
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
        scanForResponse("~weather lajdlfjlasjdf", "only places on Earth");
    }

    @Test
    public void cityWithSpaces() throws Exception {
        super.scanForResponse("~weather New York", "Weather for");

    }

    @Test
    public void weatherFromGoogle() throws Exception {
        super.scanForResponse("~weather from google for New York", "Weather for");
    }

    @Test
    public void weatherFromWeatherUnderground() throws Exception {
        super.scanForResponse("~weather from wu for Winnipeg", "Weather for");
    }

    @Test
    public void weatherFromUknown() throws Exception {
        //the weather service should still return something because it will just default to
        //trying all weather services until it gets a result
        super.scanForResponse("~weather from somethingelse for Winnipeg", "Weather for");
    }

}
