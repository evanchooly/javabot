package javabot.operations.time;

public class TimezonesBrazil implements Timezones {

    private static Tri<String> timezones = new Tri<String>( );

    static {
        for( Timezones timezone: Timezones.values( ) ) {
            timezones.insert( timezone.name( ), timezone.getTimezone( ) );
        }
    }

    private enum Timezones {
        Boa_Vista( "America/Boa_Vista" ),
        Campo_Grande( "America/Campo_Grande" ),
        Cuiaba( "America/Cuiaba" ),
        Manaus( "America/Manaus" ),
        Porto_Velho( "America/Porto_Velho" ),
        Rio_Branco( "America/Rio_Branco" ),
        Araguaina( "America/Araguaina" ),
        Salvador( "America/Bahia" ),
        Fortaleza( "America/Fortaleza" ),
        Maceio( "America/Maceio" ),
        Recife( "America/Recife" ),
        Sao_Paulo( "America/Sao_Paulo" ),
        Noronha( "America/Noronha" );

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
        return Timezones.Sao_Paulo.getTimezone( );
    }
}
