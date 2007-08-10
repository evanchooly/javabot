package javabot.operations;

import javabot.dao.ClazzDao;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

/**
 * Created Aug 9, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
@Test(groups = {"operations"})
public class JavadocOperationTest extends BaseOperationTest {
    @SpringBeanByType
    private ClazzDao clazzDao;

    public void string() {
        testOperation("javadoc String", "cheeser, please see java.lang.String: http://java.sun.com/javase/6/"
            + "docs/api/java/lang/String.html", "Should have found class");
        testOperation("javadoc java.lang.String", "cheeser, please see java.lang.String: http://java.sun.com/javase/6/"
            + "docs/api/java/lang/String.html", "Should have found class");
    }

    public void methods() {
        testOperation("javadoc String.split(*)", new String[]{
            "cheeser, please see java.lang.String.split(java.lang.String): "
                + "http://java.sun.com/javase/6/docs/api/java/lang/String.html#split(java.lang.String)",
            "cheeser, please see java.lang.String.split(java.lang.String, int): "
                + "http://java.sun.com/javase/6/docs/api/java/lang/String.html#split(java.lang.String,%20int)",
        }, "Should have found method");
        testOperation("javadoc String.split(String)", "cheeser, please see java.lang.String.split(java.lang.String): "
            + "http://java.sun.com/javase/6/docs/api/java/lang/String.html#split(java.lang.String)",
            "Should have found method");
        testOperation("javadoc String.split(java.lang.String)", "cheeser, please see java.lang.String.split(java.lang.String): "
            + "http://java.sun.com/javase/6/docs/api/java/lang/String.html#split(java.lang.String)",
            "Should have found method");

    }

    @Override
    protected BotOperation getOperation() {
        return new JavadocOperation(clazzDao);
    }
}
