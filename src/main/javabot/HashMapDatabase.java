package javabot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

/**
 * Created Jun 26, 2005
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public class HashMapDatabase extends AbstractDatabase implements Database {
    private static Log log = LogFactory.getLog(HashMapDatabase.class);
    private String _factoidFilename;
    private String _changeLog;
    private PrintWriter _factoidLog;

    public HashMapDatabase(Element root) throws IOException {
        this(root.getChild("factoids").getAttributeValue("htmlfilename"),
            root.getChild("factoids").getAttributeValue("filename"),
            root.getChild("factoids").getAttributeValue("factoidChangeLog"));
    }

    public HashMapDatabase(String htmlFile, String factoidFilename, String changeLog)
        throws IOException {
        super(htmlFile);
        _factoidFilename = factoidFilename;
        _changeLog = changeLog;
        loadFactoids();
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
            log.error(e.getMessage(), e);
            throw new ApplicationException("Could not save change to log.");
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
            log.error(exception.getMessage(), exception);
        }
    }

    private void loadFactoids() throws IOException {
        if(new File(_factoidFilename).exists()) {
            FileInputStream fis = new FileInputStream(_factoidFilename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                map = (Map)ois.readObject();
                ois.close();
                fis.close();
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
            } catch(ClassNotFoundException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
