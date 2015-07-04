package javabot.operations.time;

public class TimezonesCongo implements Timezones {

    private static Tri<String> timezones = new Tri<String>( );

    static {
        for( Timezones timezone: Timezones.values( ) ) {
            timezones.insert( timezone.name( ), timezone.getTimezone( ) );
        }
    }

    private enum Timezones {
        Kinshasa( "Africa/Kinshasa" ),
        Lubumbashi( "Africa/Lubumbashi"  );

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
        return Timezones.Kinshasa.getTimezone( );
    }
}
