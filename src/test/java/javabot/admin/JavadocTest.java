package javabot.admin;

import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import javabot.dao.ApiDao;
import javadoc.JavadocParserTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created Oct 26, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test
public class JavadocTest extends AdminOperationTest {
    @Autowired
    private ApiDao dao;

    public void parseJDK() {
        final BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "admin javadoc "
            + JavadocParserTest.API_NAME + " " + JavadocParserTest.API_URL_STRING);
        final List<Message> list = getOperation().handleMessage(event);
        Assert.assertNotNull(dao.find(JavadocParserTest.API_NAME));
    }
}
