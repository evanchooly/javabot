package javabot;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import org.jdom.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created Jun 26, 2005
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class HashMapDatabase extends AbstractDatabase implements Database {
    private static Log log = LogFactory.getLog(HashMapDatabase.class);
    private String _factoidFilename;
    private String _changeLog;
    private PrintWriter _factoidLog;

    public HashMapDatabase(Element root) {
        super(root);
        Element factoidsNode = root.getChild("factoids");
        _factoidFilename = factoidsNode.getAttributeValue("filename");
        _changeLog = factoidsNode.getAttributeValue("factoidChangeLog");
    }

    public Map getMap() {
        return map;
    }

    private Map map = new HashMap();

    public boolean hasFactoid(String key) {
        return map.containsKey(key);
    }

    public String getFactoid(String key) {
        return (String)map.get(key);
    }

    public void forgetFactoid(String sender, String key) {
        String old = (String)map.get(key);
        map.remove(key);
        saveFactoids();
        logFactoidChange(sender, key, old, "removed");
    }

    public void addFactoid(String sender, String key, String value) {
        map.put(key, value);
        saveFactoids();
        logFactoidChange(sender, key, value, "added");
    }

    private synchronized void logFactoidChange(String sender, String key, String value,
        String operation) {
        try {
            _factoidLog = new PrintWriter(new FileWriter(_changeLog, true));
            _factoidLog.println("<br> " + new Date() + ": " + sender + " " +
                operation + " " + key + " = '" + value + "'");
            _factoidLog.flush();
            _factoidLog.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the number of factoids that this bot stores.
     */
    public int getNumberOfFactoids() {
        return map.size();
    }

    public Set<String> keys() {
        return getMap().keySet();
    }

    private void saveFactoids() {
        try {
            FileOutputStream fos = new FileOutputStream
                (_factoidFilename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            oos.close();
            fos.close();
            dumpHTML();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    private void loadFactoids() {
        try {
            FileInputStream fis = new FileInputStream(_factoidFilename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (Map)ois.readObject();
            ois.close();
            fis.close();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        Set keySet = new HashSet(map.keySet());
        Iterator iterator = keySet.iterator();
        while(iterator.hasNext()) {
            String next = (String)iterator.next();
            if(!next.equals(next.toLowerCase())) {
                String value = (String)map.get(next);
                map.remove(next);
                map.put(next.toLowerCase(), value);
            }
        }
    }

}
