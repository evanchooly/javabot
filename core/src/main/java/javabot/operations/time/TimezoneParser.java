package javabot.operations.time;

public class TimezoneParser {

    public Timezone getTimezone( Country country, String province ) {
        Timezone timezone;

        if ( country.isMultiZones( ) ) {
            Timezones timezones = country.getMultiZones( );
            String timezoneId = timezones.get( province );

            if ( timezoneId != null ) {
                timezone = new Timezone( timezoneId, province );
            } else {
                timezone = new Timezone( timezones.getCapital( ), country.getName( ) );
            }
        } else {
            timezone = new Timezone( country.getTimezone( ), country.getName( ) );
        }
        return timezone;
    }

    public class Timezone {
        String timezone;
        String region;

        public Timezone( String timezone, String region ) {
            this.timezone = timezone;
            this.region = region;
        }

        public String getTimezone( ) {
            return timezone;
        }

        public String getRegion( ) {
            return region;
        }
    }
}
