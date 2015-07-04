package javabot.operations.time;

import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;

import com.google.inject.Inject;

import javabot.operations.BaseOperationTest;

public class CountryParserTest extends BaseOperationTest {

    @Inject
    private CountryParser countryParser;

    @Test
    public void getCountryGivenCountryName( ) throws Exception {
        String key = "japan";
        Country country = countryParser.getCountry( key );
        assertEquals( country, Country.JAPAN );
    }

    @Test
    public void getCountryForNullKey( ) throws Exception {
        String key = null;
        Country country = countryParser.getCountry( key );
        assertNull( country );
    }
}
