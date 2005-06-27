package javabot.operations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javabot.Database;
import javabot.HashMapDatabase;
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
    public static final String HTML_FILE = "htmlfile.log";
    public static final String FACTOID_FILENAME = "factoids.log";
    public static final String CHANGE_LOG = "factoidChange.log";
    private static Log log = LogFactory.getLog(FactoidsTests.class);

    @Configuration(beforeTestMethod = true, afterTestMethod = true)
    public void clearLogs() {
        log.debug("Deleting logs");
        new File(HTML_FILE).delete();
        new File(FACTOID_FILENAME).delete();
        new File(CHANGE_LOG).delete();
    }

    @Test(groups = {"operations"})
    public void addFactoid() {
        for(Database database : getDatabases()) {
            String key = "test factoid";
            String value = "test value";
            database.addFactoid("cheeser", key, value);
            Assert.assertEquals(value, database.getFactoid(key),
                database.getClass().getName() + ":  The factoid value should have matched");
            Assert.assertNotSame("test value2", database.getFactoid(key),
                database.getClass().getName() + ":  The factoid value should have matched");
            Assert.assertEquals(1, database.getNumberOfFactoids(), "Should have only 1 factoid");
        }
    }

    @Test(groups = {"operations"}, dependsOnMethods = {"addFactoid"})
    public void removeFactoid() {
        for(Database database : getDatabases()) {
            String key = "remove factoid";
            String value = "remove value";
            database.addFactoid("cheeser", key, value);
            database.forgetFactoid("cheeser", key);
            Assert.assertNull(database.getFactoid(key),
                database.getClass().getName() + ":  The factoid value should be null");
            Assert.assertFalse(database.hasFactoid(key));
            Assert.assertEquals(0, database.getNumberOfFactoids(), "Should have 0 factoids");
        }
    }

    public List<Database> getDatabases() {
        List<Database> databases = new ArrayList<Database>();
        try {
            databases.add(new HashMapDatabase(FactoidsTests.HTML_FILE,
                FactoidsTests.FACTOID_FILENAME,
                FactoidsTests.CHANGE_LOG));
        } catch(IOException e) {
            log.error(e.getMessage(), e);
            Assert.fail("Could not create HashMapDatabase");
        }
        return databases;
    }
}
