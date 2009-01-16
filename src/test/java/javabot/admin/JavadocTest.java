package javabot.admin;

import java.util.List;

import javabot.BotEvent;
import javabot.Message;
import org.testng.annotations.Test;
import javadoc.JavadocParserTest;

/**
 * Created Oct 26, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test
public class JavadocTest extends AdminOperationTest {
    public void parseJDK() {
        BotEvent event = new BotEvent(CHANNEL, SENDER, LOGIN, HOSTNAME, "admin javadoc "
            + JavadocParserTest.API_NAME + " " + JavadocParserTest.API_URL_STRING + " java javax" );
        List<Message> list = getOperation().handleMessage(event);
        System.out.println("list = " + list);
    }
}
