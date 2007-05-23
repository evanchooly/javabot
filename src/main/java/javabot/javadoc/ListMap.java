package javabot.javadoc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ListMap {
    private Map<Object, List<Object>> map = new HashMap<Object, List<Object>>();

    public void put(Object key, Object value) {
        List<Object> l = map.get(key);
        if(l == null) {
            l = new LinkedList<Object>();
        }
        l.add(value);
        map.put(key, l);
    }

    public List get(Object key) {
        return (List)map.get(key);
    }
}
