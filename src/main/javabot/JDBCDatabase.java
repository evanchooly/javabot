package javabot;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

/**
 * Created Jun 27, 2005
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public class JDBCDatabase extends AbstractDatabase {
    private static Log log = LogFactory.getLog(JDBCDatabase.class);
    private Properties _properties;
    private static final String DRIVER_NAME = "database.driver";
    private static final String DATABASE_URL = "database.url";
    private static final String DATABASE_USER_NAME = "database.username";
    private static final String DATABASE_PASSWORD = "database.password";
    private static final String TABLE_NAME = " factoids ";
    private static final String FACTOID_INSERT = "INSERT INTO " + TABLE_NAME
        + " (name, value, username, updated) VALUES (?, ?, ?, ?)";
    private static final String FACTOID_FETCH = "SELECT * FROM " + TABLE_NAME
        + " WHERE name=?";
    private static final String FACTOID_COUNT = "SELECT COUNT(*) FROM " + TABLE_NAME;
    private static final String FACTOID_DELETE = "DELETE FROM " + TABLE_NAME
        + " WHERE name=?";
    private static final String FACTOID_FETCH_ALL = "SELECT * FROM " + TABLE_NAME;
    public static final String SERIALIZED_MAP = "map.serialized";
    public static final String HTML_FILE = "htmlfile.log";
    private static final String FACTOID_UPDATE = "UPDATE factoids SET value=?,"
        + " username=?, updated=? WHERE id=?";
    private static final String FACTOID_CHANGE_LOG = "INSERT INTO changes"
        + " (message, changeDate) VALUES (?,?)";
    private static final String FIND_CHANGES = "SELECT * FROM changes WHERE message=?";

    public JDBCDatabase() throws IOException {
        this(HTML_FILE);
    }

    public JDBCDatabase(Element root) throws IOException {
        this(root.getChild("factoids").getAttributeValue("htmlfilename"));
    }

    public JDBCDatabase(String htmlFile) throws IOException {
        super(htmlFile);
        _properties = new Properties();
        _properties.load(getClass().getClassLoader().getResourceAsStream(
            Javabot.JAVABOT_PROPERTIES));
        try {
            Class<?> aClass = Class.forName(_properties.getProperty(DRIVER_NAME));
        } catch(ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e.getMessage());
        }
        // let's make sure we can connect
        getConnection();
    }

    private Connection getConnection() {
        try {
            Connection connection = DriverManager
                .getConnection(_properties.getProperty(DATABASE_URL),
                    _properties.getProperty(DATABASE_USER_NAME),
                    _properties.getProperty(DATABASE_PASSWORD));
            connection.setAutoCommit(true);
            return connection;
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(e.getMessage());
        }
    }

    public boolean hasFactoid(String key) {
        return getFactoid(key) != null;
    }

    public void addFactoid(String sender, String key, String value) {
        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(FACTOID_INSERT);
            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.setString(3, sender);
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stmt.execute();
        } catch(SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            cleanup(stmt, connection);
        }
        logAdd(sender, key, value);
        dumpHTML();
    }

    private void logAdd(String sender, String key, String value) {
        logChange(sender + " added '" + key + "' with a value of '"
            + value + "'");
    }

    public void updateFactoid(Factoid factoid) {
        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(FACTOID_UPDATE);
            int col = 1;
            stmt.setString(col++, factoid.getValue());
            stmt.setString(col++, factoid.getUser());
            stmt.setTimestamp(col++, new Timestamp(System.currentTimeMillis()));
            stmt.setLong(col++, factoid.getID());
            stmt.executeUpdate();
        } catch(SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            cleanup(stmt, connection);
        }
        logChange(factoid.getUser() + " changed '" + factoid.getName() + "' to '"
            + factoid.getValue() + "'");
        dumpHTML();
    }

    private void cleanup(PreparedStatement stmt, Connection connection) {
        try {
            if(stmt != null) {
                stmt.close();
            }
            if(connection != null) {
                connection.close();
            }
        } catch(SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void forgetFactoid(String sender, String key) {
        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(FACTOID_DELETE);
            stmt.setString(1, key);
            stmt.execute();
            logForget(sender, key);
            dumpHTML();
        } catch(SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            cleanup(stmt, connection);
        }
    }

    private void logForget(String sender, String key) {
        logChange(sender + " removed '" + key + "'");
    }

    private void logChange(String message) {
        PreparedStatement stmt = null;
        Connection connection = null;
        Factoid factoid = null;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(FACTOID_CHANGE_LOG);
            stmt.setString(1, message);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch(SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            cleanup(stmt, connection);
        }
    }

    public Factoid getFactoid(String key) {
        PreparedStatement stmt = null;
        Connection connection = null;
        Factoid factoid = null;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(FACTOID_FETCH);
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                factoid = createFactoid(rs);
            }
        } catch(SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            cleanup(stmt, connection);
        }
        return factoid;
    }

    public int getNumberOfFactoids() {
        PreparedStatement stmt = null;
        Connection connection = null;
        int count = 0;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(FACTOID_COUNT);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                count = rs.getInt(1);
            }
        } catch(SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            cleanup(stmt, connection);
        }
        return count;
    }

    public List<Factoid> getFactoids() {
        PreparedStatement stmt = null;
        Connection connection = null;
        List<Factoid> factoids = new ArrayList<Factoid>();
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(FACTOID_FETCH_ALL);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                factoids.add(createFactoid(rs));
            }
        } catch(SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            cleanup(stmt, connection);
        }
        Collections.sort(factoids, new Comparator<Factoid>() {
            public int compare(Factoid factoid, Factoid factoid1) {
                return factoid.getName().compareTo(factoid1.getName());
            }
        });
        return factoids;
    }

    private Factoid createFactoid(ResultSet rs) throws SQLException {
        return new Factoid(rs.getLong("id"), rs.getString("name"),
            rs.getString("value"), rs.getString("username"),
            new Date(rs.getDate("updated").getTime()));
    }

    public boolean findLog(String s) {
        PreparedStatement stmt = null;
        Connection connection = null;
        boolean found = false;
        try {
            connection = getConnection();
            stmt = connection.prepareStatement(FIND_CHANGES);
            stmt.setString(1, s);
            ResultSet rs = stmt.executeQuery();
            found = rs.next();
        } catch(SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            cleanup(stmt, connection);
        }
        return found;
    }
}
