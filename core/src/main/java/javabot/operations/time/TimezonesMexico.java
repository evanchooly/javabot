package javabot.operations.time;

public class TimezonesMexico implements Timezones {

    private static Tri<String> timezones = new Tri<String>( );

    static {
        for( Timezones timezone: Timezones.values( ) ) {
            timezones.insert( timezone.name( ), timezone.getTimezone( ) );
        }
    }

    private enum Timezones {
        Tijuana( "America/Tijuana" ),
        Hermosillo( "America/Hermosillo" ),
        Chihuahua( "Antarctica/Syowa" ),
        Mazatlan( "America/Mazatlan" ),
        Mexico_City( "America/Mexico_City" );

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
        return Timezones.Mexico_City.getTimezone( );
    }
}
