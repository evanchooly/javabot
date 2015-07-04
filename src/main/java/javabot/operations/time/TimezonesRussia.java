package javabot.operations.time;

public class TimezonesRussia implements Timezones {

    private static Tri<String> timezones = new Tri<String>( );

    static {
        for( Timezones timezone: Timezones.values( ) ) {
            timezones.insert( timezone.name( ), timezone.getTimezone( ) );
        }
    }

    private enum Timezones {
        Kaliningrad( "Europe/Kaliningrad" ),
        Moscow( "Europe/Moscow" ),
        Samara( "Europe/Samara" ),
        Yekaterinburg( "Asia/Yekaterinburg" ),
        Omsk( "Asia/Omsk" ),
        Krasnoyarsk( "Asia/Krasnoyarsk" ),
        Irkutsk( "Asia/Irkutsk" ),
        Yakutsk( "Asia/Yakutsk" ),
        Yuzhno_Sakhalinsk( "Asia/Vladivostok" ),
        Petropavlovsk_Kamchatskiy( "Asia/Kamchatka" ),
        Magadan( "Asia/Magadan" );

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
        return Timezones.Moscow.getTimezone( );
    }
}
