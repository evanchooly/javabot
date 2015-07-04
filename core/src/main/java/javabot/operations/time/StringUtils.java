package javabot.operations.time;

public class StringUtils {
    public String capitalizeFirstCharacter( String word ) {
        word = word.trim();
        return word.toUpperCase( ).charAt( 0 ) + word.substring( 1 ).toLowerCase( );
    }
}
