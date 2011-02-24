package javabot.operations;

import javabot.BaseTest;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

@Test(dependsOnGroups = {"javadoc"})
public class JavadocOperationTest extends BaseOperationTest {
    @Autowired
    private ApiDao apiDao;
    @Autowired
    private ClazzDao clazzDao;

    public void methods() {
        scanForResponse("~javadoc String.split(String)", "[JDK: java.lang.String.split(String)]");
        scanForResponse("~javadoc String.split(java.lang.String)", "[JDK: java.lang.String.split(String)]");
        scanForResponse(String.format("%s %s", getJavabot().getPircBot().getNick(), "javadoc String.split(*)"),
            "[JDK: java.lang.String.split(String)]");
    }

    public void nestedClasses() {
        scanForResponse("~javadoc Map.Entry", "[JDK: java.util.Map.Entry]");
    }

    public void format() {
        scanForResponse("~javadoc String.format(*)", "[JDK: java.lang.String.format(Locale,String,Object[])]");
    }

    public void doFinal() {
        scanForResponse("~javadoc Cipher.doFinal(*)",
            BaseTest.TEST_USER + ", too many results found.  Please see your private messages for results");
    }

    public void fields() {
        scanForResponse("~javadoc Integer.MAX_VALUE", "[JDK: java.lang.Integer.MAX_VALUE:int]");
        scanForResponse("~javadoc System.in", "[JDK: java.lang.System.in:java.io.InputStream]");
    }
}