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
    public void cityNotFound() throws Exception {
        scanForResponse("~weather lajdlfjlasjdf", "only places on Earth");

    }
}
