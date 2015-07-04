package javabot.operations.time;

public class TimezonesCanada implements Timezones {

    private static Tri<String> timezones = new Tri<String>( );

    static {
        for( CanadianProvince province: CanadianProvince.values( ) ) {
            String timezone = province.getTimezone( );
            timezones.insert( province.name( ).toLowerCase( ), timezone );
            timezones.insert( province.getAbbreviation( ).toLowerCase( ), timezone );
            timezones.insert( province.getCapital( ), timezone );
        }
    }

    public enum CanadianProvince {
        ALBERTA( 1, "AB", "Edmonton", "America/Edmonton" ),
        BRITISH_COLUMBIA( 2, "BC", "Victoria", "America/Vancouver" ),
        MANITOBA( 3, "MB", "Winnipeg", "America/Winnipeg" ),
        NEW_BRUNSWICK( 4, "NB", "Fredericton", "America/Halifax" ),
        NEWFOUNDLAND( 5, "NF", "St.John's", "America/St_Johns" ),
        NORTHWEST_TERRITORIES( 13, "NT", "Yellowknife", "America/Yellowknife" ),
        NOVA_SCOTIA( 7, "NS", "Halifax", "America/Halifax" ),
        NUNAVUT( 14, "NU", "Iqaluit", "America/Iqaluit" ),
        ONTARIO( 8, "ON", "Ottawa", "America/Toronto" ),
        PRINCE_EDWARD_ISLAND( 9, "PE", "Charlottetown", "America/Halifax" ),
        QUEBEC( 10, "QC", "Quevec", "America/Montreal" ),
        SASKATCHEWAN( 11, "SK", "Regina", "America/Regina" ),
        YUKON_TERRITORY( 12, "YT", "Whitehorse", "America/Whitehorse" );

        private final int regionCode;
        private final String abbreviation;
        private final String capital;
        private final String timezone;

        CanadianProvince( final int regionCode, final String abbreviation, final String capital,
                final String timezone ) {
            this.regionCode = regionCode;
            this.abbreviation = abbreviation;
            this.capital = capital;
            this.timezone = timezone;
        }

        public String getTimezone( ) {
            return timezone;
        }

        public String getCapital( ) {
            return capital;
        }

        public String getAbbreviation( ) {
            return abbreviation;
        }
    }

    @Override
    public String get( String province ) {
        return timezones.get( province );
    }

    @Override
    public String getCapital( ) {
        return CanadianProvince.ONTARIO.getTimezone( );
    }
}
