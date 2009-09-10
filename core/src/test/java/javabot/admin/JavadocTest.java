package javabot.admin;

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

    @Test
    public void reprocessNonExistentApi() {
        final Api api = dao.find(JavadocParserTest.API_NAME);
        if (api != null) {
            dao.delete(api);
        }
        final String message = "admin reprocessApi --name=" + JavadocParserTest.API_NAME;
        testMessage(message);
        waitForResponse("I don't know anything about " + JavadocParserTest.API_NAME);
    }

    @Test(dependsOnMethods = "reprocessNonExistentApi")
    public void processApi() {
        final String message = "admin addApi --name=" + JavadocParserTest.API_NAME + " --url="
            + JavadocParserTest.API_URL_STRING + " --version=2.3";
        testMessage(message);
        waitForResponse("done adding javadoc for " + JavadocParserTest.API_NAME);
    }

    @Test(dependsOnMethods = "processApi")
    public void dropApi() {
        final String message = "admin dropApi --name=" + JavadocParserTest.API_NAME;
        testMessage(message);
        waitForResponse("done removing javadoc for " + JavadocParserTest.API_NAME);
    }
}
