package javabot.operations;

import com.google.inject.Inject;
import javabot.operations.locator.JCPJSRLocator;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

@Test(groups = {"operations"})
public class JSROperationTest extends BaseOperationTest {
    @Inject
    JCPJSRLocator locator;

    @Test
    public void testLocatorConfig() {
        assertNotNull(locator);
    }

    @Test
    public void testJSROperations() {
        testMessage("~jsr 220", "'JSR 220: Enterprise JavaBeansTM 3.0' "
                + "can be found at http://www.jcp.org/en/jsr/detail?id=220");
    }

    @Test
    public void testBadJSRRequest() {
        testMessage("~jsr 2202213", "I'm sorry, I can't find a JSR 2202213");
    }

    @Test
    public void testNullJSROperations() {
        testMessage("~jsr", "Please supply a JSR number to look up.");
        testMessage("~jsr ", "Please supply a JSR number to look up.");
        testMessage("~jsr abc", "'abc' is not a valid JSR reference.");
    }

}
