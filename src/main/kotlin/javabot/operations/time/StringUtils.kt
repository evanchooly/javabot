package javabot.operations.time

public class StringUtils {
    public fun capitalizeFirstCharacter(word: String): String {
        var word = word
        word = word.trim()
        return word.toUpperCase().charAt(0) + word.substring(1).toLowerCase()
    }
}
