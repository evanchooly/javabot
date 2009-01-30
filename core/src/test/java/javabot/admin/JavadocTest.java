package javabot.admin;

import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.javadoc.Api;
import javadoc.JavadocParserTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

@Test
public class JavadocTest extends AdminOperationTest {
    private static final Logger log = LoggerFactory.getLogger(JavadocTest.class);
    @Autowired
    private ApiDao dao;
    @Autowired
    private ClazzDao clazzDao;

    public void parseJDK() {
        final Api api = dao.find(JavadocParserTest.API_NAME);
        if (api == null) {
            final String message = "admin addApi " + JavadocParserTest.API_NAME + " "
                + JavadocParserTest.API_URL_STRING + " java javax";
            testMessage(message, "adding javadoc for " + JavadocParserTest.API_NAME);
            waitForResponse("done adding javadoc for " + JavadocParserTest.API_NAME);
        }
    }
}
