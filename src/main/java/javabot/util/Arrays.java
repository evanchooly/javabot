package javabot.util;

import java.util.ArrayList;

/**
 * Created Jun 3, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public class Arrays {
    public static String[] subset(String[] array, int i, int i1) {
        String[] copy = new String[i1 - i];
        for(int index = i; i < i1; index++) {
            copy[index - i] = array[index];
        }
        return copy;
    }

    public static String toString(String[] array, String delim) {
        StringBuilder builder = new StringBuilder();
        for(String elem : array) {
            if(builder.length() != 0) {
                builder.append(delim);
            }
            builder.append(elem);
        }

        return builder.toString();
    }

    public static String[] removeAll(String[] words, String toRemove) {
        ArrayList<String> copy = new ArrayList<String>();
        for(String word : words) {
            if(! word.equals(toRemove)) {
                copy.add(word);
            }
        }

        return copy.toArray(new String[copy.size()]);
    }

    public static int search(String[] messageParts, String s) {
        boolean found = false;
        int index = -1;
        while(! (found = messageParts[++index].equals(s) && index < messageParts.length));
        return found ? index : -1;
    }
}
