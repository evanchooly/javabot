package javabot.admin;

import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.javadoc.Api;
import javadoc.JavadocParserTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

@Test
public class JavadocTest extends AdminOperationTest {
    @Autowired
    private ApiDao dao;
    @Autowired
    private ClazzDao clazzDao;

    public void parseJDK() {
        final Api api = dao.find(JavadocParserTest.API_NAME);
        if (api == null) {
            final String message = "admin addApi " + JavadocParserTest.API_NAME + " "
                + JavadocParserTest.API_URL_STRING + " java javax";
            testMessage(message);
            waitForResponse("done adding javadoc for " + JavadocParserTest.API_NAME);
//        } else {
//            testMessage("admin dropApi " + JavadocParserTest.API_NAME,
//                "removing old " + JavadocParserTest.API_NAME + " javadoc");
//            waitForResponse(
//                "done removing old " + JavadocParserTest.API_NAME + " javadoc");
        }
    }
}
