package javabot.operations;

import com.jayway.awaitility.Duration;
import javabot.Messages;
import javabot.dao.ApiDao;
import javabot.dao.EventDao;
import javabot.javadoc.JavadocApi;
import javabot.model.ApiEvent;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

@Test//(dependsOnMethods = {"jdk"})
public class JavadocOperationTest extends BaseOperationTest {
    @Inject
    private ApiDao apiDao;
    @Inject
    private EventDao eventDao;
    @Inject
    private Messages messages;

    @BeforeTest
    public void getBot() {
        getJavabot();
    }

    public void jdk() throws MalformedURLException {
        JavadocApi api = apiDao.find("JDK");
        if (api == null) {
            ApiEvent event = new ApiEvent(getTestUser().getNick(), "JDK", "http://docs.oracle.com/javase/8/docs/api",
                                          null);
            eventDao.save(event);
            waitForEvent(event, "adding JDK", new Duration(30, TimeUnit.MINUTES));
        }
        messages.get();

    }

    public void constructors() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc java.lang.String(char[])", "[JDK: java.lang.String.String(char[])]");
        scanForResponse("~javadoc String(char[])", "[JDK: java.lang.String.String(char[])]");
    }

    public void methods() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc String.split(String)", "[JDK: java.lang.String.split(String)]");
        scanForResponse("~javadoc -jdk String.split(String)", "[JDK: java.lang.String.split(String)]");
        scanForResponse("~javadoc String.split(java.lang.String)", "[JDK: java.lang.String.split(String)]");
        scanForResponse("~javadoc String.join(*)", "[JDK: java.lang.String.join");
        scanForResponse(String.format("%s %s", getJavabot().getNick(), "javadoc String.split(*)"), "[JDK: java.lang.String.split(String)]");
    }

    public void nestedClasses() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc Map.Entry", "[JDK: java.util.Map.Entry]");
    }

    public void format() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc String.format(*)", "[JDK: java.lang.String.format(Locale, String, Object...)]");
    }

    @Test
    public void doFinal() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc String.valueOf(*)",
                        getTestUser().getNick() + ", too many results found.  Please see your private messages for results");
    }

    public void fields() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc System.in", "[JDK: java.lang.System#in:java.io.InputStream]");
        scanForResponse("~javadoc Integer.MAX_VALUE", "[JDK: java.lang.Integer#MAX_VALUE:int]");
        scanForResponse("~javadoc -jdk System.in", "[JDK: java.lang.System#in:java.io.InputStream]");
    }

    public void inherited() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc ArrayList.listIterator(*)", "[JDK: java.util.ArrayList.listIterator(int)]");
    }
}