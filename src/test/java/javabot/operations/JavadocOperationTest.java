package javabot.operations;

import com.antwerkz.sofia.Sofia;
import com.jayway.awaitility.Duration;
import javabot.BaseMessagingTest;
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
public class JavadocOperationTest extends BaseMessagingTest {
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
        scanForResponse("~javadoc java.lang.String(char[])", "java/lang/String.html");
        scanForResponse("~javadoc String(char[])", "java/lang/String.html#String-char[]-");
    }

    public void methods() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc String.split(String)", "java/lang/String.html#split-java.lang.String-");
        scanForResponse("~javadoc -jdk String.split(String)", "java/lang/String.html#split-java.lang.String-");
        scanForResponse("~javadoc String.split(java.lang.String)", "java/lang/String.html#split-java.lang.String-");
        scanForResponse("~javadoc String.join(*)", "java/lang/String.html#join-");
        scanForResponse(String.format("%s %s", getJavabot().getNick(), "javadoc String.split(*)"),
                        "java/lang/String.html#split-java.lang.String-");
    }

    public void nestedClasses() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc Map.Entry", "java/util/Map.Entry.html");
    }

    public void format() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc String.format(*)", "java/lang/String.html#format-java.util.Locale-java.lang.String-java.lang.Object...");
    }

    @Test
    public void doFinal() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc String.valueOf(*)", Sofia.tooManyResults(getTestUser().getNick()));
    }

    public void fields() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc System.in", "java/lang/System.html#in");
        scanForResponse("~javadoc Integer.MAX_VALUE", "java/lang/Integer.html#MAX_VALUE");
        scanForResponse("~javadoc -jdk System.in", "java/lang/System.html#in");
    }

    public void inherited() throws MalformedURLException {
        jdk();
        scanForResponse("~javadoc ArrayList.listIterator(*)", "java/util/ArrayList.html#listIterator-int-");
    }
}