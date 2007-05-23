package javabot;

import java.io.IOException;

import org.testng.annotations.Test;

/**
 * Created Jun 27, 2005
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public class JDBCDatabaseTest {

    @Test(groups = {"database"})
    public void connectToDatabase() throws IOException {
        JDBCDatabase database = getDatabase();
    }

    private JDBCDatabase getDatabase() throws IOException {
        return new JDBCDatabase(JDBCDatabase.HTML_FILE);
    }
}
