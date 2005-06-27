package javabot;

import java.util.Map;
import java.util.Set;

public interface Database {
    boolean hasFactoid(String key);

    void addFactoid(String sender, String key, String value);

    void forgetFactoid(String sender, String key);

    String getFactoid(String key);

    Map getMap();

    int getNumberOfFactoids();

    Set<String> keys();
}
