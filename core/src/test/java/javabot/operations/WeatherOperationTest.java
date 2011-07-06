package javabot.operations;

import javabot.dao.WeatherDao;
import org.springframework.beans.factory.annotation.Autowired;
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
        super.scanForResponse("~weather Winnipeg", "Weather for");
    }

    @Test
    public void cityNotFound() throws Exception {
        super.scanForResponse("~weather lajdlfjlasjdf", "only places on Earth");

    }
}
