package javabot.operations.time;

public class TimezonesAustralia implements Timezones {

    private static Tri<String> timezones = new Tri<String>( );

    static {
        for( AustralianState states: AustralianState.values( ) ) {
            String timezone = states.getTimezone( );
            timezones.insert( states.name( ).toLowerCase( ), timezone );
            timezones.insert( states.getAbbreviation( ).toLowerCase( ), timezone );
            timezones.insert( states.getCapital( ), timezone );
        }
    }

    public enum AustralianState {
        WESTERN_AUSTRALIA( 8, "WA", "Perth", "Australia/Perth" ),
        SOUTH_AUSTRALIA( 5, "SA", "Adelaide", "Australia/Adelaide" ),
        NORTH_TERRITORY( 3, "NT", "Darwin", "Australia/Darwin" ),
        QUEENSLAND( 4, "QLD", "Brisbane", "Australia/Brisbane" ),
        NEW_SOUTH_WALES( 2, "NSW", "Sydney", "Australia/Sydney" ),
        AUSTRALIAN_CAPITAL_TERRITORY( 1, "ACT", "Canberra", "Australia/Canberra"  ),
        VICTORIA( 7, "VIC", "Melbourne", "Australia/Victoria" ),
        TASMANIA( 6, "TAS", "Hobart", "Australia/Hobart" );

        private final String timezone;
        private final String capital;
        private final String abbreviation;

        AustralianState( final int regionCode, final String abbreviation, final String capital,
                final String timezone ) {
            this.abbreviation = abbreviation;
            this.timezone = timezone;
            this.capital = capital;
        }

        public String getAbbreviation( ) {
            return abbreviation;
        }

        public String getCapital( ) {
            return capital;
        }

        public String getTimezone( ) {
            return timezone;
        }

    }

    @Override
    public String get( String province ) {
        return timezones.get( province );
    }

    @Override
    public String getCapital( ) {
        return AustralianState.NEW_SOUTH_WALES.getTimezone( );
    }
}
