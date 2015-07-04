package javabot.operations.time;

public class TimezonesAntarctica implements Timezones{

    private static Tri<String> timezones = new Tri<String>( );

    static {
        for( Timezones timezone: Timezones.values( ) ) {
            timezones.insert( timezone.name( ), timezone.getTimezone( ) );
        }
    }

    private enum Timezones {
        Palmer( "Antarctica/Palmer" ),
        Rothera( "Antarctica/Rothera" ),
        Syowa( "Antarctica/Syowa" ),
        Mawson( "Antarctica/Mawson" ),
        Vostok( "Antarctica/Vostok" ),
        Davis( "Antarctica/Davis" ),
        Casey( "Antarctica/Casey" ),
        Dumont_D_Urville( "Antarctica/DumontDUrville" );

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
        return Timezones.Palmer.getTimezone( );
    }
}
