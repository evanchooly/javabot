package javabot;

import java.util.Map;
import java.util.Set;

/**
 * Provides a generic interface to a factoid database.
 */
public interface Database {
    boolean hasFactoid(String key);

    void addFactoid(String sender, String key, String value);

    void forgetFactoid(String sender, String key);

    String getFactoid(String key);

    int getNumberOfFactoids();

    Map<String, String> getMap();
}
