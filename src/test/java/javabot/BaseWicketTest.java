package javabot;

import javax.servlet.ServletContext;

import org.testng.annotations.BeforeMethod;
import org.apache.wicket.util.tester.WicketTester;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import javabot.admin.AdminApplication;

/**
 * Created Jul 29, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public class BaseWicketTest extends AbstractTransactionalSpringContextTests {
    private static final String[] LOCATIONS = {
        "classpath:applicationContext.xml",
        "classpath:applicationContext-test.xml"
    };
    private WicketTester tester;

    @Override
    protected String[] getConfigLocations() {
        return LOCATIONS;
    }

    @BeforeMethod
    public void init() {
        tester = new WicketTester(new TestAdminApplication());
    }

    @Override
    protected ConfigurableApplicationContext loadContext(Object locations) throws Exception {
//        ++this.loadCount;
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocations((String[])locations);
        context.refresh();
        return context;
    }

    private class TestAdminApplication extends AdminApplication {
        @Override
        public void init() {
            ServletContext servletContext = getServletContext();
            XmlWebApplicationContext context = (XmlWebApplicationContext)applicationContext;
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
            context.setServletContext(servletContext);
            super.init();
        }
    }
}
