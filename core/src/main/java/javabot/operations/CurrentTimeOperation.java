package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import com.antwerkz.maven.SPI;
import com.google.inject.Inject;

import javabot.IrcEvent;
import javabot.Message;
import javabot.operations.time.Country;
import javabot.operations.time.CountryParser;
import javabot.operations.time.DateUtils;
import javabot.operations.time.StringUtils;
import javabot.operations.time.TimezoneParser;
import javabot.operations.time.TimezoneParser.Timezone;

/**
 * Usage convention<br/>
 * Countries with single timezone: ~time in [country_name]<br/>
 * Countries with multiple timezones: ~time in [country_name], [province/state]<br/>
 */
@SPI(BotOperation.class)
public class CurrentTimeOperation extends BotOperation {

    @Inject
    CountryParser countryParser;
    @Inject
    TimezoneParser timezoneParser;
    @Inject
    DateUtils dateUtils;
    @Inject
    StringUtils stringUtils;

    private static final String key = "time in";

    @Override
    public List<Message> handleMessage( final IrcEvent event ) {
        final String message = event.getMessage( );
        final List<Message> responses = new ArrayList<Message>( );

        if( message.contains( key ) ) {
            InputLocale locale = extractLocale( message );

            Country country = countryParser.getCountry( locale.getCountry( ) );
            String result;

            if ( country == null ) {
                result = "Location you entered is either not supported or does not exist.";
            } else {
                Timezone timezone = timezoneParser.getTimezone( country, locale.getRegion( ) );
                result = new StringBuilder( )
                        .append( "Current time in " )
                        .append( stringUtils.capitalizeFirstCharacter( timezone.getRegion( ) ) )
                        .append( " is " )
                        .append( dateUtils.getCurrentDateAtZone( timezone.getTimezone( ) ) )
                        .append( "." ).toString( );
            }

            if ( result != null ) {
                responses.add( new Message( event.getChannel( ), event, result ) );
            }
        }
        return responses;
    }

    private InputLocale extractLocale( String raw ) {
        String extracted = raw.substring( raw.indexOf( key ) + key.length( ) );
        String[] inputs = extracted.split( "," );

        if ( inputs.length == 1 ) {
            return new InputLocale( inputs[0], null );
        } else if ( inputs.length == 2 ) {
            return new InputLocale( inputs[0], inputs[1] );
        }
        return new InputLocale( null, null );
    }

    private class InputLocale {
        String country;
        String region;

        public InputLocale( String country, String province ) {
            this.country = country;
            this.region = province;
        }

        public String getCountry( ) {
            return country;
        }

        public String getRegion( ) {
            return region;
        }
    }
}
