package javabot.operations.time;

import com.google.inject.Inject;
import javabot.BaseMessagingTest;
import javabot.operations.time.TimezoneParser.Timezone;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class TimezoneParserTest extends BaseMessagingTest {

    @Inject
    TimezoneParser timezoneParser;

    @Test
    public void getZoneIdGivenCountryWithOneTimezoneNoProvinceProvided( ) throws Exception {
        Country country = Country.JAPAN;
        String province = null;

        Timezone expectedTimezone = timezoneParser.new Timezone( "Asia/Tokyo", country.getName( ) );

        //Province not required for country with single timezone
        Timezone actualTimezone = timezoneParser.getTimezone( country, province );
        assertEquals( expectedTimezone.getTimezone( ), actualTimezone.getTimezone( ) );
        assertEquals( expectedTimezone.getRegion( ), actualTimezone.getRegion( ) );
    }

    @Test
    public void getZoneIdGivenCountryWithOneTimezoneProvinceProvided( ) throws Exception {
        Country country = Country.JAPAN;
        String province = "Random Province";

        Timezone expectedTimezone = timezoneParser.new Timezone( "Asia/Tokyo", country.getName( ) );

        //No effect when province is provided with country with single timezone
        Timezone actualTimezone = timezoneParser.getTimezone( country, province );
        assertEquals( expectedTimezone.getTimezone( ), actualTimezone.getTimezone( ) );
        assertEquals( expectedTimezone.getRegion( ), actualTimezone.getRegion( ) );
    }

    @Test
    public void getTimezoneGivenCountryWithMultipleZonesProvinceProvided( ) throws Exception {
        Country country = Country.CANADA;
        String province = "Manitoba";

        Timezone expectedTimezone = timezoneParser.new Timezone( "America/Winnipeg", province );

        Timezone actualTimezone = timezoneParser.getTimezone( country, province );
        assertEquals( expectedTimezone.getTimezone( ), actualTimezone.getTimezone( ) );
        assertEquals( expectedTimezone.getRegion( ), actualTimezone.getRegion( ) );
    }

    @Test
    public void getTimezoneGivenCountryWithMultipleZonesCapitalCityProvided( ) throws Exception {
        Country country = Country.CANADA;
        String province = "Winnipeg";

        Timezone expectedTimezone = timezoneParser.new Timezone( "America/Winnipeg", province );

        // If country has multiple timezones, we accept city as a second argument
        Timezone actualTimezone = timezoneParser.getTimezone( country, province );
        assertEquals( expectedTimezone.getTimezone( ), actualTimezone.getTimezone( ) );
        assertEquals( expectedTimezone.getRegion( ), actualTimezone.getRegion( ) );
    }

    @Test
    public void getTimezoneGivenCountryWithMultipleZonesProvinceNotProvided( ) throws Exception {
        Country country = Country.CANADA;
        String province = null;

        Timezone expectedTimezone = timezoneParser.new Timezone( "America/Toronto", country.getName( ) );

        //If country has multiple timezones, and province is not provided we get time at capital city
        Timezone actualTimezone = timezoneParser.getTimezone( country, province );
        assertEquals( expectedTimezone.getTimezone( ), actualTimezone.getTimezone( ) );
        assertEquals( expectedTimezone.getRegion( ), actualTimezone.getRegion( ) );
    }
}
