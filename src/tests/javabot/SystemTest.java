package javabot;

import java.io.IOException;

import org.testng.annotations.Test;
import org.jdom.JDOMException;

/**
 * Created Jun 28, 2005
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public class SystemTest {

    @Test(groups = {"system"})
    public void systemTest() throws IOException, JDOMException {
        Javabot javabot = new Javabot();
//        javabot.onMessage();
    }
}
