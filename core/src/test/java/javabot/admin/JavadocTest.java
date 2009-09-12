package javabot.admin;

import java.io.File;
import java.net.MalformedURLException;

import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.javadoc.Api;
import javabot.operations.BaseOperationTest;
import javadoc.JavadocParserTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

@Test
public class JavadocTest extends BaseOperationTest {
    @Autowired
    private ApiDao dao;
    @Autowired
    private ClazzDao clazzDao;
    private static final String API_NAME = "Servlet";
    private static final String ZIP_LOCATION = "http://repo1.maven.org/maven2/javax/servlet/servlet-api/2.3/servlet-api-2.3.jar";
    private static final String API_URL_STRING = "http://java.sun.com/products/servlet/2.3/javadoc/";

    @Test
    public void reprocessNonExistentApi() {
        final Api api = dao.find(API_NAME);
        if (api != null) {
            dao.delete(api);
        }
        final String message = "admin reprocessApi --name=" + API_NAME;
        testMessage(message);
        waitForResponse("I don't know anything about " + API_NAME);
    }

    @Test(dependsOnMethods = "reprocessNonExistentApi")
    public void processApi() {
        final String message = "admin addApi "
            + " --name=" + API_NAME
            + " --url=" + API_URL_STRING
            + " --zip=" + ZIP_LOCATION
            + " --packages=javax";
        testMessage(message);
        waitForResponse("done adding javadoc for " + API_NAME);
    }

    @Test(dependsOnMethods = "processApi")
    public void dropApi() {
        final String message = "admin dropApi --name=" + API_NAME;
        testMessage(message);
        waitForResponse("done removing javadoc for " + API_NAME);
    }
}
