package javabot;

import java.util.List;

/**
 * Provides a generic interface to a factoid database.
 */
public interface Database {
    boolean hasFactoid(String key);

    void addFactoid(String sender, String key, String value);

    void forgetFactoid(String sender, String key);

    Factoid getFactoid(String key);

    int getNumberOfFactoids();

    List<Factoid> getFactoids();
}
