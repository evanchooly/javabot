package javabot.operations.time;

public class TimezonesSpain implements Timezones {

    private static Tri<String> timezones = new Tri<String>( );

    static {
        for( Timezones timezone: Timezones.values( ) ) {
            timezones.insert( timezone.name( ), timezone.getTimezone( ) );
        }
    }

    private enum Timezones {
        Canary_Islands( "Atlantic/Canary" ),
        Ceuta( "Africa/Ceuta" ),
        Madrid( "Europe/Madrid" );

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
        return Timezones.Madrid.getTimezone( );
    }
}
