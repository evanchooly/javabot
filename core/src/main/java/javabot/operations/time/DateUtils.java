package javabot.operations.time;

import java.util.Date;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {
    private final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern( "MMMM dd" );
    private final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern( "h:mm aa" );

    public String getCurrentDateAtZone( String timezone ) {
        Long date = new Date( ).getTime( );
        String formattedDate = dateFormatter.withZone( DateTimeZone.forID( timezone ) ).print( date );
        String formattedTime = timeFormatter.withZone( DateTimeZone.forID( timezone ) ).print( date ).toLowerCase( );
        return formattedDate + " @ " + formattedTime;
    }
}
