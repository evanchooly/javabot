package javabot.admin;

import javabot.BaseWicketTest;
import org.testng.annotations.Test;

/**
 * Created Oct 26, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test
public class JavadocTest extends BaseWicketTest {
    public void submitJavaApi() {
        getTester().startPage(Home.class);
        getTester().dumpPage();
    }
}
