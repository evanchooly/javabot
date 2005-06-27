package javabot;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Configuration;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;
import javabot.operations.FactoidsTests;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created Jun 27, 2005
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public class HashMapDatabaseTest {
    public static final String SERIALIZED_MAP = "map.serialized";
    public static final String VALUE = "http://java.sun.com/j2se/1.5.0/docs/api/index.html";
    public static final String KEY = "api";
    private static Log log = LogFactory.getLog(HashMapDatabaseTest.class);

    @Configuration(beforeTestClass = true)
    public void initDatabase() throws IOException {
        if(! new File(SERIALIZED_MAP).exists()) {
            HashMapDatabase database = new HashMapDatabase(FactoidsTests.HTML_FILE,
                SERIALIZED_MAP, FactoidsTests.CHANGE_LOG);
            database.addFactoid("cheeser", KEY, VALUE);
        }
    }

    @Test(groups = {"database"})
    public void loadTest() throws IOException {
        HashMapDatabase database = new HashMapDatabase(FactoidsTests.HTML_FILE,
            SERIALIZED_MAP, FactoidsTests.CHANGE_LOG);
        Assert.assertEquals(VALUE, database.getFactoid(KEY),
            "The API factoid is missing/incorrect");
    }

    @Test
    @ExpectedExceptions({ApplicationException.class})
    public void badLog() throws Exception {
        HashMapDatabase database = new HashMapDatabase(FactoidsTests.HTML_FILE,
            SERIALIZED_MAP, FactoidsTests.CHANGE_LOG + "/?");
        database.addFactoid("cheeser", KEY, VALUE);
    }
}
