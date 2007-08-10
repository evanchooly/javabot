package javabot.operations;

import javabot.dao.ClazzDao;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;
import org.unitils.spring.annotation.SpringBean;
import org.unitils.spring.annotation.SpringBeanByName;

/**
 * Created Aug 9, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
@Test(groups = {"operations"})
public class JavadocOperationTest extends BaseOperationTest {
    @SpringBeanByName
    private ClazzDao clazzDao;

    public void string() {
        testOperation("javadoc String", "I never knew about asdfghjkl anyway, " + SENDER + ".",
            "Should have found class");
    }

    @Override
    protected BotOperation getOperation() {
        return new JavadocOperation(clazzDao);
    }
}
