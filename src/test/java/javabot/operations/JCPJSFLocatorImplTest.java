package javabot.operations;

import javabot.operations.locator.impl.JCPJSRLocatorImpl;
import org.testng.annotations.Test;

/**
 * Created Dec 11, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test
public class JCPJSFLocatorImplTest {
    public void jsr315() {
        JCPJSRLocatorImpl locator = new JCPJSRLocatorImpl();
        String title = locator.findInformation(315);
        System.out.println("title = " + title);
    }

    public static void main(String[] args) {
        new JCPJSFLocatorImplTest().jsr315();
    }
}
