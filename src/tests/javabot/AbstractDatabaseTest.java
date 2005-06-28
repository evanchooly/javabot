package javabot;

import java.io.IOException;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created Jun 28, 2005
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public class AbstractDatabaseTest {
    private static Log log = LogFactory.getLog(AbstractDatabaseTest.class);
    
    @Test(groups = {"utility"})
    public void htmlize() throws IOException {
        AbstractDatabase database = new JDBCDatabase();
        String html = database.htmlize("hi http://example.com there");
        log.debug("html = \n" + html);
        Assert.assertEquals("hi <a href=\"http://example.com\" "
            + "target=\"_blank\">http://example.com</a> there", html,
            "The link should be HTMLized");

        html = database.htmlize("hi http://example.com http://example.com there");
        log.debug("html = \n" + html);
        Assert.assertEquals("hi <a href=\"http://example.com\" "
            + "target=\"_blank\">http://example.com</a> <a href=\"http://example.com\" "
            + "target=\"_blank\">http://example.com</a> there", html,
            "The links should be HTMLized");
    }
}
