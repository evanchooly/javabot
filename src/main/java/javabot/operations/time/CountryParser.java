package javabot.operations.time;

public class CountryParser {
    private static Tri<Country> tri = new Tri<Country>( );

    static {
        for( Country country : Country.values( ) ) {
            tri.insert( country.getName( ), country );
            tri.insert( country.getAbbreviation( ), country );
        }
    }

    public Country getCountry( String key ) {
        return tri.get( key );
    }
}
