package javabot.operations;

import javabot.operations.locator.impl.JCPJSRLocatorImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created Dec 11, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test
public class JCPJSFLocatorImplTest {
    public void jsr315() {
        final JCPJSRLocatorImpl locator = new JCPJSRLocatorImpl();
        Assert.assertEquals(locator.findInformation(315),
            "'JSR 315: JavaTM Servlet 3.0 Specification' can be found at http://www.jcp.org/en/jsr/detail?id=315");
    }
}