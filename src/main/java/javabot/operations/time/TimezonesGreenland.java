package javabot.operations.time;

public class TimezonesGreenland implements Timezones {

    private static Tri<String> timezones = new Tri<String>( );

    static {
        for( Timezones timezone: Timezones.values( ) ) {
            timezones.insert( timezone.name( ), timezone.getTimezone( ) );
        }
    }

    private enum Timezones {
        Thule( "America/Thule" ),
        Godthab( "America/Godthab" ),
        Scoresbysund( "America/Scoresbysund" ),
        Danmarkshavn( "America/Danmarkshavn" );

        private String timezone;

        Timezones( String timezone ) {
            this.timezone = timezone;
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
        return Timezones.Godthab.getTimezone( );
    }
}
