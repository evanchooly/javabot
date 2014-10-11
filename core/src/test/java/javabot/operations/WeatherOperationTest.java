package javabot.operations;

import org.testng.annotations.Test;

/**
 * Integration test for the Weather Operation, will actually attempt to contact the Google API for weather as
 * it's using the real Dao instead of a Mock/Stub
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
        scanForResponse("~weather lajdlfjlasjdf", "only supports places on Earth");
    }

    @Test
    public void cityWithSpaces() throws Exception {
        super.scanForResponse("~weather New York", "Weather for");

    }

}
