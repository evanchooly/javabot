package javabot.operations.time;

//TODO handle states with multiple timezones
public class TimezonesUS implements Timezones {

    private static Tri<String> timezones = new Tri<String>( );

    static {
        for( UsState state: UsState.values( ) ) {
            String timezone = state.getTimezone( );
            timezones.insert( state.name( ).toLowerCase( ), timezone );
            timezones.insert( state.getAbbreviation( ).toLowerCase( ), timezone );
            timezones.insert( state.getCapital( ), timezone );
        }
    }

    public enum UsState {
        ALABAMA( 1, "AL", "Montgomery", "US/Central" ),
        ALASKA( 2, "AK", "Juneau", "US/Alaska" ),
        ARIZONA( 4, "AZ", "Phoenix", "America/Phoenix" ),
        ARKANSAS( 5, "AR", "Little Rock", "America/Chicago" ),
        CALIFORNIA( 6, "CA", "Sacramento", "America/Los_Angeles" ),
        COLORADO( 8, "CO", "Denver", "America/Denver" ),
        CONNECTICUT( 9, "CT", "Hartford", "US/Eastern" ),
        DELAWARE( 10, "DE", "Dover", "US/Eastern" ),
        DISTRICT_OF_COLUMBIA( 11, "DC", "Washington DC", "US/Eastern" ),
        FLORIDA( 12, "FL", "Tallahassee", "US/Eastern" ),
        GEORGIA( 13, "GA", "Atlanta", "US/Eastern" ),
        HAWAII( 15, "HI", "Honolulu", "Pacific/Honolulu" ),
        IDAHO( 16, "ID", "Boise", "America/Boise" ),
        ILLINOIS( 17, "IL", "Springfield", "US/Central" ),
        INDIANA( 18, "IN", "Indianapolis", "US/Eastern" ),
        IOWA( 19, "IA", "Des Moines", "US/Central" ),
        KANSAS( 20, "KS", "Topeka", "US/Central" ),
        KENTUCKY( 21, "KY", "Frankfort", "US/Eastern" ),
        LOUISIANA( 22, "LA", "Baton Rouge", "US/Central" ),
        MAINE( 23, "ME", "Augusta", "US/Eastern" ),
        MARYLAND( 24, "MD", "Annapolis", "US/Eastern" ),
        MASSACHUSETTS( 25, "MA", "Boston", "US/Eastern" ),
        MICHIGAN( 26, "MI", "Lansing", "US/Eastern" ),
        MINNESOTA( 27, "MN", "Saint Paul", "US/Central" ),
        MISSISSIPPI( 28, "MS", "Jackson", "US/Central" ),
        MISSOURI( 29, "MO", "Jefferson City", "US/Central" ),
        MONTANA( 30, "MT", "Helena", "US/Mountain" ),
        NEBRASKA( 31, "NE", "Lincoln", "US/Central" ),
        NEVADA( 32, "NV", "Carson City", "US/Pacific" ),
        NEW_HAMPSHIRE( 33, "NH", "Concord", "US/Eastern" ),
        NEW_JERSEY( 34, "NJ", "Trenton", "US/Eastern" ),
        NEW_MEXICO( 35, "NM", "Santa Fe", "US/Mountain" ),
        NEW_YORK( 36, "NY", "Albany", "US/Eastern" ),
        NORTH_CAROLINA( 37, "NC", "Raleigh", "US/Eastern" ),
        NORTH_DAKOTA( 38, "ND", "Bismarck",  "US/Central" ),
        OHIO( 39, "OH", "Columbus", "US/Eastern" ),
        OKLAHOMA( 40, "OK", "Oklahoma City", "US/Central" ),
        OREGON( 41, "OR", "Salem", "US/Pacific" ),
        PENNSYLVANIA( 42, "PA", "Harrisburg", "US/Eastern" ),
        PUERTO_RICO( 72, "PR", "San Juan", "America/Puerto_Rico" ),
        RHODE_ISLAND( 44, "RI", "Providence", "US/Eastern" ),
        SOUTH_CAROLINA( 45, "SC", "Columbia", "US/Eastern" ),
        SOUTH_DAKOTA( 46, "SD", "Pierre", "US/Eastern" ),
        TENNESSEE( 47, "TN", "Nashville", "US/Central" ),
        TEXAS( 48, "TX", "Austin", "US/Central" ),
        UTAH( 49, "UT", "Salt Lake City", "US/Mountain" ),
        VERMONT( 50, "VT", "Montpelier", "US/Eastern" ),
        VIRGINIA( 51, "VA", "Richmond", "US/Eastern" ),
        WASHINGTON( 53, "WA", "Olympia", "US/Pacific" ),
        WEST_VIRGINIA( 54, "WV", "Charleston", "US/Eastern" ),
        WISCONSIN( 55, "WI", "Madison", "US/Central" ),
        WYOMING( 56, "WY", "Cheyenne", "US/Mountain" );

        private final int regionCode;
        private final String abbreviation;
        private final String capital;
        private final String timezone;

        UsState( final int regionCode, final String abbreviation, final String capital, final String timezone ) {
            this.regionCode = regionCode;
            this.abbreviation = abbreviation;
            this.capital = capital;
            this.timezone = timezone;
        }

        public String getCapital( ) {
            return capital;
        }

        public String getTimezone( ) {
            return timezone;
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
    public String getCapital() {
        return UsState.DISTRICT_OF_COLUMBIA.getTimezone( );
    }
}
