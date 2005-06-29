package javabot.operations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javabot.Database;
import javabot.JDBCDatabase;
import javabot.Factoid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.Configuration;
import org.testng.annotations.Test;

/**
 * Created Jun 26, 2005
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public class FactoidsTests {
    public static final String FACTOID_FILENAME = "factoids.log";
    public static final String CHANGE_LOG = "factoidChange.log";
    private static Log log = LogFactory.getLog(FactoidsTests.class);

    @Configuration(beforeTestMethod = true)
    public void clearLogs() {
        new File(JDBCDatabase.HTML_FILE).delete();
        new File(FACTOID_FILENAME).delete();
        new File(CHANGE_LOG).delete();
    }

    @Test(groups = {"operations"})
    public void addFactoid() {
        for(Database database : getDatabases()) {
            String key = "test factoid";
            String value = "test value";
            int count = database.getNumberOfFactoids();
            database.addFactoid("cheeser", key, value);
            Factoid factoid = database.getFactoid(key);
            Assert.assertEquals(value, factoid.getValue(),
                database.getClass().getName() + ":  The factoid value should have matched");
            Assert.assertNotSame("test value2", factoid.getValue(),
                database.getClass().getName() + ":  The factoid value should have matched");
            Assert.assertEquals(count + 1, database.getNumberOfFactoids(), "Should have only 1 more factoid");
        }
    }

    @Test(groups = {"operations"}, dependsOnMethods = {"addFactoid"})
    public void removeFactoid() {
        for(Database database : getDatabases()) {
            String key = "remove factoid";
            String value = "remove value";
            database.addFactoid("cheeser", key, value);
            int count = database.getNumberOfFactoids();
            database.forgetFactoid("cheeser", key);
            Assert.assertNull(database.getFactoid(key),
                database.getClass().getName() + ":  The factoid value should be null");
            Assert.assertFalse(database.hasFactoid(key),
                database.getClass().getName() + ":  The factoid not be present");
            Assert.assertEquals(count - 1, database.getNumberOfFactoids(),
                database.getClass().getName() + ":  Should have 1 fewer factoids");
        }
    }

    public List<Database> getDatabases() {
        List<Database> databases = new ArrayList<Database>();
        try {
            databases.add(new JDBCDatabase(JDBCDatabase.HTML_FILE));
        } catch(IOException e) {
            log.error(e.getMessage(), e);
            Assert.fail("Could not create database");
        }
        return databases;
    }
}
